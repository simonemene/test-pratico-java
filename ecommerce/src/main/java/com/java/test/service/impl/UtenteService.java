package com.java.test.service.impl;

import com.java.test.annotation.ReadOnlyTransactional;
import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.IUtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UtenteService implements IUtenteService {

	private final UtenteRepository repository;

	private final UtenteMapper mapper;

	@Transactional
	@Override
	public UtenteDto creazioneUtente(UtenteDto utente) {
		UtenteEntity utenteDaSalvare = mapper.toEntity(utente);
		UtenteEntity utenteSalvato;
		try {
			utenteSalvato = repository.save(utenteDaSalvare);
		}catch(DataIntegrityViolationException e)
		{
			throw new ApplicationException("L'utente esiste già",e);
		}catch(DataAccessException e)
		{
			throw new ApplicationException("Errore generico nella crezione utente",e);
		}
		return mapper.toDto(utenteSalvato);
	}

	@ReadOnlyTransactional
	@Override
	public List<UtenteDto> prendiListaClienti() {
		return mapper.toListaDto(repository.findAll());
	}
}
