package com.java.test.service;

import com.java.test.dto.UtenteRequestDto;
import com.java.test.entity.RuoloEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.enums.RuoloEnum;
import com.java.test.exception.ApplicationException;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.RuoloRepository;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.impl.UtenteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;
import java.util.Optional;

@ExtendWith(OutputCaptureExtension.class)
public class UtenteServiceUnitTest {

	@Mock
	private UtenteRepository repository;

	@Mock
	private UtenteMapper mapper;

	@InjectMocks
	private UtenteService service;

	@Mock
	private RuoloRepository ruoloRepository;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void creazioneUtenteErrore(CapturedOutput capturedOutput)
	{
		//given
		Mockito.when(ruoloRepository.findByRuolo(Mockito.any())).thenReturn(Optional.of(new RuoloEntity(RuoloEnum.ROLE_USER)));
		UtenteRequestDto utente = new UtenteRequestDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S","password");
		UtenteEntity entity = new UtenteEntity("prova@prova.com","Paolo","Rossi","DRI456JHKGIT976S","password",new RuoloEntity(RuoloEnum.ROLE_USER));
		Mockito.when(mapper.toEntity(utente)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(UtenteEntity.class))).thenThrow(new DataIntegrityViolationException("Vincolo violato nella creazione utente"));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.creazioneUtente(utente))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Impossibile creare l'utente")
				.hasCauseInstanceOf(DataIntegrityViolationException.class);
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
		Assertions.assertThat(capturedOutput.getOut()).contains("Vincolo violato nella creazione utente");
	}

	@Test
	public void creazioneUtenteErroreGenerico(CapturedOutput capturedOutput)
	{
		//given
		UtenteRequestDto utente = new UtenteRequestDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S","password");
		UtenteEntity entity = new UtenteEntity("prova@prova.com","Paolo","Rossi","DRI456JHKGIT976S","password",new RuoloEntity(RuoloEnum.ROLE_USER));
		Mockito.when(mapper.toEntity(utente)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(UtenteEntity.class))).thenThrow(new BadSqlGrammarException("","",new SQLException()));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.creazioneUtente(utente))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Errore generico nella crezione utente")
				.hasCauseInstanceOf(DataAccessException.class);;
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
		Assertions.assertThat(capturedOutput.getOut()).contains("Errore generico crezione utente ");
	}
}
