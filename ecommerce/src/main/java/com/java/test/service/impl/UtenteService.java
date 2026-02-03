package com.java.test.service.impl;

import com.java.test.annotation.ReadOnlyTransactional;
import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.IUtenteService;
import lombok.RequiredArgsConstructor;
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
		return mapper.toDto(repository.save(utenteDaSalvare));
	}

	@ReadOnlyTransactional
	@Override
	public List<UtenteDto> prendiListaClienti() {
		return mapper.toListaDto(repository.findAll());
	}
}
