package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.test.baseconfig.ControllerBaseConfig;
import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

public class ProdottoControllerSecurityIntegrationTest  extends ControllerBaseConfig {

	@Autowired
	private TestRestTemplate template;

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void creazioneProdotto401() throws JsonProcessingException {
		//given
		ProdottoRequestDto request = new ProdottoRequestDto(new BigDecimal("1.2"),
				"computer");
		//when
		ResponseEntity<ProdottoResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.postForEntity("/api/prodotto", request, ProdottoResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void creazioneProdotto403() throws JsonProcessingException {
		//given
		ProdottoRequestDto request = new ProdottoRequestDto(new BigDecimal("1.2"),
				"computer");
		//when
		ResponseEntity<ProdottoResponseDto> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.postForEntity("/api/prodotto", request, ProdottoResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}


	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiProdotto401() {
		//given
		//when
		ResponseEntity<ProdottoListResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.getForEntity("/api/prodotto", ProdottoListResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}


	@Sql(scripts = "classpath:sql/service/prodotti/insert-prodotti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiInformazioneProdotto401() {
		//given
		//when
		ResponseEntity<ProdottoResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.getForEntity("/api/prodotto/{id}", ProdottoResponseDto.class,
						"rgvbdfgdf454345");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

	}
}
