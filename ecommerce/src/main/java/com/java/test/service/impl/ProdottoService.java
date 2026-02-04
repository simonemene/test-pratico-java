package com.java.test.service.impl;

import com.java.test.annotation.ReadOnlyTransactional;
import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import com.java.test.entity.ProdottoEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.repository.ProdottoRepository;
import com.java.test.service.IProdottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProdottoService implements IProdottoService {

	private final ProdottoRepository repository;

	private final ProdottoMapper mapper;

	@Transactional
	@Override
	public ProdottoResponseDto inserisciUnProdotto(
			ProdottoRequestDto prodottoDaInserire) {
		ProdottoEntity prodottoDaSalvare = mapper.toEntity(prodottoDaInserire);
		ProdottoEntity prodottoSalvato;
		try {
			prodottoSalvato = repository.save(prodottoDaSalvare);
		}catch(DataIntegrityViolationException e)
		{
			log.error("Vincolo violato nella creazione del prodotto {}",e.getMessage(),e);
			throw new ApplicationException("Vincolo violato nella creazione prodotto",e);
		}catch(DataAccessException e)
		{
			log.error("Errore generico crezione del prodotto {}",e.getMessage(),e);
			throw new ApplicationException("Errore generico nella crezione del prodotto",e);
		}
		return mapper.toDto(prodottoSalvato);
	}

	@ReadOnlyTransactional
	@Override
	public ProdottoListResponseDto prendiListaProdotti() {
		return mapper.toListaDto(repository.findAll());
	}
}
