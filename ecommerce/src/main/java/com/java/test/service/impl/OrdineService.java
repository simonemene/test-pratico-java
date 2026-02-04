package com.java.test.service.impl;

import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.entity.OrdineEntity;
import com.java.test.entity.ProdottoEntity;
import com.java.test.entity.StockEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.exception.ProdottoException;
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
	public OrdineEffettuatoResponseDto effettuaOrdine(String utenteId,String prodottoId, int quantita) {

		StockEntity stock = stockRepository.findByProdotto_ProductId(prodottoId)
				.orElseThrow(()-> new ProdottoException("Nessun prodotto trovato per id: " + prodottoId));
		UtenteEntity utente = utenteRepository.findByUtenteId(utenteId)
				.orElseThrow(()->new UtenteException("Nessun utente trovato per id: " + utenteId));
		OrdineEffettuatoResponseDto dto;
		try
		{
			OrdineEntity ordine = crezioneOrdine(utente,stock.getProdotto(),quantita);
			ordine = repository.save(ordine);
			stock.modificaQuantitaMagazzino(quantita);
			dto = new OrdineEffettuatoResponseDto(prodottoMapper.toDtoStock(stock.getProdotto(),stock),ordine.getOrdineId());
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

	private OrdineEntity crezioneOrdine(UtenteEntity utente,ProdottoEntity prodotto,int quantita)
	{
		OrdineEntity ordine = new OrdineEntity(utente);
		ordine.aggiungiProdotto(prodotto,quantita);
		return ordine;
	}
}
