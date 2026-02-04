package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import com.java.test.entity.ProdottoEntity;
import com.java.test.repository.ProdottoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@AutoConfigureTestDatabase
public class ProdottoServiceIntegrationTest extends TestjavaApplicationTests {

	@Autowired
	private ProdottoRepository repository;

	@Autowired
	private IProdottoService service;


	@Test
	public void inserisciProdotto()
	{
		//given
		ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		//when
		ProdottoResponseDto prodottoSalvato = service.inserisciUnProdotto(prodotto);
		//then
		ProdottoEntity prodottoSalvatoEntity = repository.findByProductId(prodottoSalvato.productId());

		Assertions.assertThat(prodottoSalvato).isNotNull();
		Assertions.assertThat(prodottoSalvatoEntity.getId()).isGreaterThan(0L);
		Assertions.assertThat(prodottoSalvato.prezzo()).isEqualTo(prodottoSalvatoEntity.getPrezzo());
		Assertions.assertThat(prodottoSalvato.nome()).isEqualTo(prodottoSalvatoEntity.getNome());
	}

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
			,config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.INFERRED))
	@Test
	public void prendiTuttiIProdotti()
	{
		//given
		//when
		ProdottoListResponseDto prodotti = service.prendiListaProdotti();
		//then
		Assertions.assertThat(prodotti.prodotti().size()).isEqualTo(4);
		ProdottoResponseDto computer = prodotti.prodotti().stream().filter(prodotto->
				prodotto.nome().equals("COMPUTER")).findFirst().get();
		Assertions.assertThat(computer.prezzo()).isEqualTo(new BigDecimal("12.40"));
		Assertions.assertThat(computer.nome()).isEqualTo("COMPUTER");
		Assertions.assertThat(computer.productId()).isEqualTo("dgfgdfgdf45mnbv");
	}
}
