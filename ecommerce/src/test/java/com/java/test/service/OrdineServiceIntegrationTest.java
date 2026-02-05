package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.ProdottoConQuantitaResponseDto;
import com.java.test.entity.StockEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

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


	@Transactional
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

		StockEntity stock = stockRepository.findByProdotto_ProductId("dgfgdfgdfaaa4345").get();
		Assertions.assertThat(stock.getProdotto().getNome()).isEqualTo("CELLULARE");
		Assertions.assertThat(stock.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("222.32"));
		Assertions.assertThat(stock.getQuantita()).isEqualTo(1);

	}
}
