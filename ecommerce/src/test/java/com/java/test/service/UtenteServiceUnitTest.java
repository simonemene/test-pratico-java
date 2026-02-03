package com.java.test.service;

import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.impl.UtenteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;

public class UtenteServiceUnitTest {

	@Mock
	private UtenteRepository repository;

	@Mock
	private UtenteMapper mapper;

	@InjectMocks
	private UtenteService service;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void creazioneUtenteErrore()
	{
		//given
		UtenteDto utente = new UtenteDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S");
		UtenteEntity entity = new UtenteEntity("prova@prova.com","Paolo","Rossi","DRI456JHKGIT976S");
		Mockito.when(mapper.toEntity(utente)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(UtenteEntity.class))).thenThrow(new DataIntegrityViolationException("L'utente esiste già"));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.creazioneUtente(utente))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("L'utente esiste già");
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
	}

	@Test
	public void creazioneUtenteErroreGenerico()
	{
		//given
		UtenteDto utente = new UtenteDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S");
		UtenteEntity entity = new UtenteEntity("prova@prova.com","Paolo","Rossi","DRI456JHKGIT976S");
		Mockito.when(mapper.toEntity(utente)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(UtenteEntity.class))).thenThrow(new BadSqlGrammarException("","",new SQLException()));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.creazioneUtente(utente))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Errore generico nella crezione utente");
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
	}
}
