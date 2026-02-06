package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.*;
import com.java.test.entity.StockEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@AutoConfigureTestDatabase
public class OrdineServiceIntegrationTest extends TestjavaApplicationTests {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private OrdineRepository ordineRepository;

	@Autowired
	private IOrdineService service;

	@Autowired
	private JdbcClient jdbcClient;


	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void effettuaOrdine()
	{
		//given
		UtenteEntity utente = utenteRepository.findByUtenteId("dffgdfdddddgbgbfgb454534").get();
		//when
		OrdineEffettuatoResponseDto response = service.effettuaOrdine(utente.getUtenteId(),
				Map.of(
						"dgfgdfgdfaaa4345",2,
						"dgfgdwwdf454345",1

		));
		//then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.prodotto().prodottoModificati().size()).isEqualTo(2);
		ProdottoConQuantitaResponseDto prodotto = response.prodotto().prodottoModificati().stream()
				.filter(prod->prod.productId().equals("dgfgdfgdfaaa4345")).findFirst().get();
		Assertions.assertThat(prodotto.nome()).isEqualTo("CELLULARE");
		Assertions.assertThat(prodotto.prezzo()).isEqualTo(new BigDecimal("222.32"));

		StockEntity stockCellulare = stockRepository.findByProdotto_ProductId("dgfgdfgdfaaa4345").get();
		Assertions.assertThat(stockCellulare.getProdotto().getNome()).isEqualTo("CELLULARE");
		Assertions.assertThat(stockCellulare.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("222.32"));
		Assertions.assertThat(stockCellulare.getQuantita()).isEqualTo(1);

		StockEntity stockCaricaBatterie = stockRepository.findByProdotto_ProductId("dgfgdwwdf454345").get();
		Assertions.assertThat(stockCaricaBatterie.getProdotto().getNome()).isEqualTo("CARICA BATTERIE");
		Assertions.assertThat(stockCaricaBatterie.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("10.30"));
		Assertions.assertThat(stockCaricaBatterie.getQuantita()).isEqualTo(6);

	}


	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-singolo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void ricercaOrdinePerId()
	{
		//given
		String ordineId = "123A";
		//when
		OrdineResponseDto response = service.ricercaOrdinePerId(ordineId);
		//then
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.movimenti()).isNotEmpty();
		Assertions.assertThat(response.movimenti().size()).isEqualTo(1);
		List<MovimentoResponseDto> movimenti = response.movimenti();
		MovimentoResponseDto movimento = movimenti.stream().filter(mov->mov.nomeProdotto().equals("CARICA CELLULARE")).findFirst().get();
		Assertions.assertThat(movimento.quantitaOrdinata()).isEqualTo(3);

		Assertions.assertThat(response.idPubblicoOrdine()).isEqualTo("123A");
		Assertions.assertThat(response.utenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");
	}

	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void ricercaTuttiGliOrdini()
	{
		//given
		//when
		OrdiniResponseDto ordini = service.ricercaTuttiGliOrdini();
		//then
		Assertions.assertThat(ordini.ordini().size()).isEqualTo(2);
		OrdineResponseDto ordine = ordini.ordini().stream().filter(ord->ord.idPubblicoOrdine().equals("123A")).findFirst().get();
		Assertions.assertThat(ordine.movimenti()).isNotEmpty();
		Assertions.assertThat(ordine.movimenti().size()).isEqualTo(1);
		List<MovimentoResponseDto> movimenti = ordine.movimenti();
		MovimentoResponseDto movimento = movimenti.stream().filter(mov->mov.nomeProdotto().equals("CARICA CELLULARE")).findFirst().get();
		Assertions.assertThat(movimento.quantitaOrdinata()).isEqualTo(3);

		Assertions.assertThat(ordine.idPubblicoOrdine()).isEqualTo("123A");
		Assertions.assertThat(ordine.utenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");

		OrdineResponseDto ordine2 = ordini.ordini().stream().filter(ord->ord.idPubblicoOrdine().equals("564W")).findFirst().get();
		Assertions.assertThat(ordine2.movimenti()).isNotEmpty();
		Assertions.assertThat(ordine2.movimenti().size()).isEqualTo(2);
		List<MovimentoResponseDto> movimenti2 = ordine2.movimenti();
		MovimentoResponseDto movimento1 = movimenti2.stream().filter(mov->mov.nomeProdotto().equals("COMPUTER")).findFirst().get();
		Assertions.assertThat(movimento1.quantitaOrdinata()).isEqualTo(10);
		MovimentoResponseDto movimento2 = movimenti2.stream().filter(mov->mov.nomeProdotto().equals("CELLULARE")).findFirst().get();
		Assertions.assertThat(movimento2.quantitaOrdinata()).isEqualTo(2);

		Assertions.assertThat(ordine2.idPubblicoOrdine()).isEqualTo("564W");
		Assertions.assertThat(ordine2.utenteId()).isEqualTo("dffgdfgfgbfttttgb454534");
	}

	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void modificaNumeriProdotto()
	{
		//given
		String id = "564W";
		Map<String, Integer> prodotti = Map.of("dgfgdfgdf45mnbv",2);
		//when
		service.modificaProdotti(id,prodotti);
		//then
		Assertions.assertThat(jdbcClient.sql("SELECT QUANTITA FROM MOVIMENTO WHERE " +
								"ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" +
								" AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
				.param(1,"dgfgdfgdf45mnbv")
				.param(2,id)
						.query().singleValue())
				.isEqualTo(12);
		StockEntity stock = stockRepository.findByProdotto_ProductId("dgfgdfgdf45mnbv").get();
		Assertions.assertThat(stock.getQuantita()).isEqualTo(10);
	}

	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void eliminaProdottiDaOrdine()
	{
		//given
		String id = "564W";
		List<String> prodotti = List.of("dgfgdfgdf45mnbv");
		//when
		service.eliminaProdotti(id,prodotti);
		//then
		Assertions.assertThat(jdbcClient.sql("SELECT FLG_ANNULLO FROM MOVIMENTO WHERE " +
								"ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" +
								" AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1,"dgfgdfgdf45mnbv")
						.param(2,id)
						.query().singleValue())
				.isEqualTo("S");
		StockEntity stock = stockRepository.findByProdotto_ProductId("dgfgdfgdf45mnbv").get();
		Assertions.assertThat(stock.getQuantita()).isEqualTo(22);
	}

	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void inserisciProdottiDaOrdine()
	{
		//given
		String id = "564W";
		Map<String, Integer> prodotti = Map.of("rgvbdfgdf454345",2);
		Assertions.assertThat(jdbcClient.sql("SELECT COUNT(*) FROM MOVIMENTO WHERE " +
								"  ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1,id)
						.query().singleValue())
				.isEqualTo(2L);
		//when
		service.inserisciProdotti(id,prodotti);
		//then
		Assertions.assertThat(jdbcClient.sql("SELECT COUNT(*) FROM MOVIMENTO WHERE " +
								"  ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1,id)
						.query().singleValue())
				.isEqualTo(3L);
		Assertions.assertThat(jdbcClient.sql("SELECT QUANTITA FROM MOVIMENTO WHERE " +
								"ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" +
								" AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1,"rgvbdfgdf454345")
						.param(2,id)
						.query().singleValue())
				.isEqualTo(2);
		StockEntity stock = stockRepository.findByProdotto_ProductId("rgvbdfgdf454345").get();
		Assertions.assertThat(stock.getQuantita()).isEqualTo(3);
	}





}
