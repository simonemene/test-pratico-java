package com.java.test.service.impl;

import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.ProdottoConQuantitaListaDto;
import com.java.test.dto.ProdottoConQuantitaResponseDto;
import com.java.test.entity.*;
import com.java.test.exception.ApplicationException;
import com.java.test.exception.MagazzinoException;
import com.java.test.exception.UtenteException;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.IOrdineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdineService implements IOrdineService {

	private final OrdineRepository repository;

	private final UtenteRepository utenteRepository;

	private final StockRepository stockRepository;

	private final ProdottoMapper prodottoMapper;

	@Transactional
	@Override
	public OrdineEffettuatoResponseDto effettuaOrdine(String utenteId, Map<String,Integer> prodotti) {


		List<ProdottoEntity> daAggiungere = new ArrayList<>();

		UtenteEntity utente = utenteRepository.findByUtenteId(utenteId)
				.orElseThrow(()->new UtenteException("Nessun utente trovato per id: " + utenteId));
		OrdineEntity ordine = new OrdineEntity(utente);
		OrdineEffettuatoResponseDto dto;
		try
		{
            for(var valore : prodotti.entrySet())
			{
				modificaProdottiEControllo(valore);
			}

			List<StockEntity> prodottiDaStock = stockRepository.findByProdotto_ProductIdIn(prodotti.keySet().stream().toList());
			List<ProdottoConQuantitaResponseDto> listProdotti = new ArrayList<>();

			for(StockEntity entity : prodottiDaStock) {

				ordine.aggiungiProdotto(entity.getProdotto(),prodotti.get(entity.getProdotto().getProductId()));
			}
				ordine = repository.save(ordine);

				for(MovimentoEntity movimento : ordine.getMovimento())
				{
					listProdotti.add(creazioneProdotto(movimento));
				}
			dto = new OrdineEffettuatoResponseDto(new ProdottoConQuantitaListaDto(listProdotti),ordine.getOrdineId());


		}catch(DataIntegrityViolationException e)
		{
			log.error("Vincolo violato nella creazione dell'ordine {}",e.getMessage(),e);
			throw new ApplicationException("Impossibile creare l'ordine",e);
		}catch(DataAccessException e)
		{
			log.error("Errore generico crezione dell'ordine {}",e.getMessage(),e);
			throw new ApplicationException("Errore generico nella creazione dell'ordine",e);
		}
        return dto;
	}


	private ProdottoConQuantitaResponseDto creazioneProdotto(MovimentoEntity movimento)
	{
		return prodottoMapper.toDtoStock(movimento.getProdotto(),movimento.getProdotto()
				.getStocK());
	}

	private void modificaProdottiEControllo(Map.Entry<String, Integer> valore)
	{
		int elementiModificati = stockRepository.modificaQuantitaProdotto(valore.getValue(),valore.getKey());
		if(elementiModificati == 0)
		{
			log.warn("""
					[ATTENZIONE] Gli elementi modificati non combaciano con il numero di prodotti attesi
					""");
			throw new MagazzinoException("Errore: il prodotto non ha giacenze in magazzino");
		}
	}

}
