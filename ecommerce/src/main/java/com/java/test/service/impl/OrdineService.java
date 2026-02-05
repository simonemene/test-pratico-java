package com.java.test.service.impl;

import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.ProdottoConQuantitaListaDto;
import com.java.test.dto.ProdottoConQuantitaResponseDto;
import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import com.java.test.entity.StockEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.exception.MagazzinoException;
import com.java.test.exception.ProdottoException;
import com.java.test.exception.UtenteException;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.IOrdineService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrdineService implements IOrdineService {

	private final OrdineRepository repository;

	private final UtenteRepository utenteRepository;

	private final StockRepository stockRepository;

	private final ProdottoMapper prodottoMapper;

	@Transactional
	@Override
	public OrdineEffettuatoResponseDto effettuaOrdine(String utenteId, Map<String,Integer> prodotti)
	{
		if(prodotti.isEmpty())
		{
			throw new ProdottoException("Nessun prodotto per l'ordine effettuato dall'utente","");
		}
		UtenteEntity utente = utenteRepository.findByUtenteId(utenteId)
				.orElseThrow(()->new UtenteException("Nessun utente trovato",utenteId));
		OrdineEntity ordine = new OrdineEntity(utente);
		OrdineEffettuatoResponseDto dto;
		try
		{
            for(var valore : prodotti.entrySet())
			{
				modificaProdottiEControllo(valore);
			}

			List<StockEntity> prodottiDaStock = stockRepository.findByProdotto_ProductIdIn(prodotti.keySet().stream().toList());
			if(prodottiDaStock.isEmpty())
			{
				throw new ProdottoException("Non riesco a trovare i prodotti per l'ordine nel sistema",
						Arrays.toString(prodotti.keySet().toArray()));
			}
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
			throw new ApplicationException("Vincolo violato nella creazione l'ordine",e);
		}catch(DataAccessException e)
		{
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
			throw new MagazzinoException("Errore: il prodotto non ha giacenze in magazzino",valore.getKey(),valore.getValue());
		}
	}

}
