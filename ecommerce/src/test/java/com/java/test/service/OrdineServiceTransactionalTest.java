package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.ProdottoConQuantitaResponseDto;
import com.java.test.entity.OrdineEntity;
import com.java.test.entity.StockEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Map;

@AutoConfigureTestDatabase
public class OrdineServiceTransactionalTest  extends TestjavaApplicationTests {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private JdbcClient jdbcClient;

	@Autowired
	private UtenteRepository utenteRepository;

	@MockitoBean
	private OrdineRepository ordineRepository;

	@Autowired
	private IOrdineService service;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
	}

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void effettuaOrdineRoolback() {
		//given
		UtenteEntity utente = utenteRepository.findByUtenteId("dffgdfdddddgbgbfgb454534")
				.get();
		Mockito.when(ordineRepository.save(Mockito.any(OrdineEntity.class))).thenThrow(new DataIntegrityViolationException("errore"));
		//when
		Assertions.assertThatThrownBy(()->service.effettuaOrdine(
				utente.getEmail(), Map.of("dgfgdfgdfaaa4345", 2, "dgfgdwwdf454345", 1

				))).isInstanceOf(ApplicationException.class)
				.hasMessageContaining("Vincolo violato nella creazione l'ordine");
		//then

		StockEntity stockCellulare = stockRepository.findByProdotto_ProductId(
				"dgfgdfgdfaaa4345").get();
		Assertions.assertThat(stockCellulare.getProdotto().getNome())
				.isEqualTo("CELLULARE");
		Assertions.assertThat(stockCellulare.getProdotto().getPrezzo())
				.isEqualTo(new BigDecimal("222.32"));
		Assertions.assertThat(stockCellulare.getQuantita()).isEqualTo(3);

		StockEntity stockCaricaBatterie = stockRepository.findByProdotto_ProductId(
				"dgfgdwwdf454345").get();
		Assertions.assertThat(stockCaricaBatterie.getProdotto().getNome())
				.isEqualTo("CARICA BATTERIE");
		Assertions.assertThat(stockCaricaBatterie.getProdotto().getPrezzo())
				.isEqualTo(new BigDecimal("10.30"));
		Assertions.assertThat(stockCaricaBatterie.getQuantita()).isEqualTo(7);

		Assertions.assertThat(jdbcClient.sql("SELECT COUNT(*) FROM ORDINE").query().singleValue()).isEqualTo(0L);
		Assertions.assertThat(jdbcClient.sql("SELECT COUNT(*) FROM MOVIMENTO").query().singleValue()).isEqualTo(0L);

	}
}
