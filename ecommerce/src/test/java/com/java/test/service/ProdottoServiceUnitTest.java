package com.java.test.service;

import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.entity.ProdottoEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.mapper.UtenteMapper;
import com.java.test.repository.ProdottoRepository;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.impl.ProdottoService;
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

import java.math.BigDecimal;
import java.sql.SQLException;

@ExtendWith(OutputCaptureExtension.class)
public class ProdottoServiceUnitTest {

	@Mock
	private ProdottoRepository repository;

	@Mock
	private ProdottoMapper mapper;

	@InjectMocks
	private ProdottoService service;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void creazioneProdottoErrore(CapturedOutput capturedOutput)
	{
		//given
		ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		ProdottoEntity entity = new ProdottoEntity(new BigDecimal("1.2"),"computer");
		Mockito.when(mapper.toEntity(prodotto)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(ProdottoEntity.class))).thenThrow(new DataIntegrityViolationException("Vincolo violato nella creazione utente"));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.inserisciUnProdotto(prodotto))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Vincolo violato nella creazione prodotto")
				.hasCauseInstanceOf(DataIntegrityViolationException.class);
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
		Assertions.assertThat(capturedOutput.getOut()).contains("Vincolo violato nella creazione del prodotto ");
	}

	@Test
	public void creazioneProdottoErroreGenerico(CapturedOutput capturedOutput)
	{
		//given
		ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		ProdottoEntity entity = new ProdottoEntity(new BigDecimal("1.2"),"computer");
		Mockito.when(mapper.toEntity(prodotto)).thenReturn(entity);
		Mockito.when(repository.save(Mockito.any(ProdottoEntity.class))).thenThrow(new BadSqlGrammarException("","",new SQLException()));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.inserisciUnProdotto(prodotto))
				.isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Errore generico nella crezione del prodotto")
				.hasCauseInstanceOf(DataAccessException.class);;
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
		Assertions.assertThat(capturedOutput.getOut()).contains("Errore generico crezione del prodotto");
	}
}
