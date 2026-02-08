package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.test.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdottoControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void creazioneProdotto() throws JsonProcessingException {
		//given
		ProdottoRequestDto request = new ProdottoRequestDto(new BigDecimal("1.2"),"computer");
		//when
		ResponseEntity<ProdottoResponseDto> response = template
				.withBasicAuth("admin@prova.com","prova")
				.postForEntity(
				"/api/prodotto",request,ProdottoResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getHeaders().getLocation().getPath())
				.isEqualTo("/api/prodotto/"+response.getBody().productId());
		Assertions.assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("productId").isEqualTo(request);

	}

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiProdotto()
	{
		//given
		//when
		ResponseEntity<ProdottoListResponseDto> response = template
				.withBasicAuth("prova1@prova.com","prova")
				.getForEntity("/api/prodotto",ProdottoListResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ProdottoListResponseDto body = response.getBody();
		Assertions.assertThat(body.prodotti().size()).isEqualTo(4);
		ProdottoResponseDto computer = body.prodotti().stream().filter(prodotto->
				prodotto.nome().equals("COMPUTER")).findFirst().get();
		Assertions.assertThat(computer.prezzo()).isEqualTo(new BigDecimal("12.40"));
		Assertions.assertThat(computer.nome()).isEqualTo("COMPUTER");
		Assertions.assertThat(computer.productId()).isEqualTo("dgfgdfgdf45mnbv");
	}

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiInformazioneProdotto()
	{
		//given
		//when
		ResponseEntity<ProdottoResponseDto> response = template
				.withBasicAuth("prova@prova.com","prova")
				.getForEntity("/api/prodotto/{id}",ProdottoResponseDto.class,"rgvbdfgdf454345");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ProdottoResponseDto prodottoDaControllare = response.getBody();
		Assertions.assertThat(prodottoDaControllare.nome()).isEqualTo("CARICA CELLULARE");
		Assertions.assertThat(prodottoDaControllare.prezzo()).isEqualTo(new BigDecimal("2.12"));
		Assertions.assertThat(prodottoDaControllare.productId()).isEqualTo("rgvbdfgdf454345");
	}

}
