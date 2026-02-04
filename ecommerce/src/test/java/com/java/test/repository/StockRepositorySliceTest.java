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
		StockEntity stockEntity = repository.findByProdotto_ProductId("rgvbdfgdf454345").get();
		//then
		Assertions.assertThat(stockEntity
				.getProdotto()).isNotNull();
		Assertions.assertThat(stockEntity.getQuantita()).isEqualTo(5);
		Assertions.assertThat(stockEntity.getProdotto().getNome()).isEqualTo("CARICA CELLULARE");
		Assertions.assertThat(stockEntity.getProdotto().getPrezzo()).isEqualTo(new BigDecimal("2.12"));
	}
}
