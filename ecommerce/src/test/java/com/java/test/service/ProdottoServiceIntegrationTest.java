package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import com.java.test.entity.ProdottoEntity;
import com.java.test.exception.ApplicationException;
import com.java.test.repository.ProdottoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@AutoConfigureTestDatabase
public class ProdottoServiceIntegrationTest extends TestjavaApplicationTests {

	@Autowired
	private ProdottoRepository repository;

	@Autowired
	private IProdottoService service;


	@Transactional
	@Test
	public void inserisciProdotto()
	{
		//given
		ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		//when
		ProdottoResponseDto prodottoSalvato = service.inserisciUnProdotto(prodotto);
		//then
		ProdottoEntity prodottoSalvatoEntity = repository.findByProductId(prodottoSalvato.productId()).get();

		Assertions.assertThat(prodottoSalvato).isNotNull();
		Assertions.assertThat(prodottoSalvatoEntity.getId()).isGreaterThan(0L);
		Assertions.assertThat(prodottoSalvato.prezzo()).isEqualTo(prodottoSalvatoEntity.getPrezzo());
		Assertions.assertThat(prodottoSalvato.nome()).isEqualTo(prodottoSalvatoEntity.getNome());
	}

	@Transactional
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

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void inserisciProdottoRollback()
	{
		//given
		ProdottoRequestDto prodotto = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		ProdottoRequestDto prodottoPerRollback = new ProdottoRequestDto(new BigDecimal("2.0"),"computer");
		//when
		service.inserisciUnProdotto(prodotto);
		//then
		Assertions.assertThatThrownBy(()->service.inserisciUnProdotto(prodottoPerRollback))
				.isInstanceOf(ApplicationException.class);
		List<ProdottoEntity> prodotti = repository.findAll();
		Assertions.assertThat(prodotti.size()).isEqualTo(1);
		Assertions.assertThatThrownBy(()->prodotti.stream().filter(check->check.getPrezzo().equals(new BigDecimal("2.0"))).findFirst().get())
				.isInstanceOf(NoSuchElementException.class);
		ProdottoEntity prodottoSalvato = prodotti.stream().filter(check->check.getNome().equals("computer")).findFirst().get();
		Assertions.assertThat(prodottoSalvato.getPrezzo()).isEqualTo(new BigDecimal("1.20"));
		Assertions.assertThat(prodottoSalvato.getNome()).isEqualTo("computer");
		Assertions.assertThat(prodottoSalvato.getProductId())
				.matches("[A-Za-z0-9\\W_]+");

	}
}
