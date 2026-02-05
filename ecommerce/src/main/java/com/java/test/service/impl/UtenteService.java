package com.java.test.service.impl;

import com.java.test.annotation.ReadOnlyTransactional;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.exception.UtenteException;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.IUtenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteService implements IUtenteService {

	private final UtenteRepository repository;

	private final UtenteMapper mapper;

	@Transactional
	@Override
	public UtenteResponseDto creazioneUtente(UtenteRequestDto utente) {
		UtenteEntity utenteDaSalvare = mapper.toEntity(utente);
		UtenteEntity utenteSalvato;
		try {
			utenteSalvato = repository.save(utenteDaSalvare);
		}catch(DataIntegrityViolationException e)
		{
			log.error("Vincolo violato nella creazione utente {}",e.getMessage(),e);
			throw new ApplicationException("Impossibile creare l'utente",e);
		}catch(DataAccessException e)
		{
			log.error("Errore generico crezione utente {}",e.getMessage(),e);
			throw new ApplicationException("Errore generico nella crezione utente",e);
		}
		return mapper.toDto(utenteSalvato);
	}

	@ReadOnlyTransactional
	@Override
	public UtenteListResponseDto prendiListaClienti() {
		return new UtenteListResponseDto(mapper.toListaDto(repository.findAll()));
	}

	@ReadOnlyTransactional
	@Override
	public UtenteResponseDto prendiInformazionUtente(String idPubblico) {
		return repository.findByUtenteId(idPubblico)
				.map(mapper::toDto)
				.orElseThrow(()->new UtenteException("Utente non trovato",idPubblico));
	}
}
