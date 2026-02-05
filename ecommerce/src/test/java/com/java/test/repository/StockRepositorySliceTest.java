package com.java.test.repository;

import com.java.test.entity.StockEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase
public class StockRepositorySliceTest {

	@Autowired
	private StockRepository repository;


	@Test
	public void trovaStockCollegatoAlProdotto()
	{
		//given
		//when
		Optional<StockEntity> stockDisponibili = repository.findByProdotto_ProductId(
				"rgvbdfgdf454345");
		//then
		StockEntity stockEntity = stockDisponibili.get();
		Assertions.assertThat(stockEntity
				.getProdotto()).isNotNull();
		Assertions.assertThat(stockEntity.getQuantita()).isEqualTo(5);
		Assertions.assertThat(stockEntity.getProdotto().getNome()).isEqualTo("CARICA CELLULARE");
		Assertions.assertThat(stockEntity.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("2.12"));
	}

	@Test
	public void trovaStockCollegatoAlProdottoIn()
	{
		//given
		//when
		List<StockEntity> stockDisponibili = repository.findByProdotto_ProductIdIn(
				List.of("rgvbdfgdf454345","dgfgdfgdfaaa4345","dffgdfgdfg"));
		//then
		Assertions.assertThat(stockDisponibili.size()).isEqualTo(2);
		StockEntity stockEntity = stockDisponibili.stream()
				.filter(stock->stock.getProdotto().getProductId().equals("rgvbdfgdf454345")).findFirst().get();
		Assertions.assertThat(stockEntity
				.getProdotto()).isNotNull();
		Assertions.assertThat(stockEntity.getQuantita()).isEqualTo(5);
		Assertions.assertThat(stockEntity.getProdotto().getNome()).isEqualTo("CARICA CELLULARE");
		Assertions.assertThat(stockEntity.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("2.12"));
	}

	@Test
	public void modificaQuantitaProdotto()
	{
		//given
		int quantita = 2;
		String productId = "rgvbdfgdf454345";
		//when
		int elementiModificati = repository.modificaQuantitaProdotto(quantita,productId);
		//then
		Assertions.assertThat(elementiModificati).isEqualTo(1);
		StockEntity stock = repository.findByProdotto_ProductId(productId).get();
		Assertions.assertThat(stock.getQuantita()).isEqualTo(3);
	}
}
