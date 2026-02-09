package com.java.test.controller;

import com.java.test.baseconfig.ControllerBaseConfig;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

public class UtenteControllerSecurityIntegrationTest  extends ControllerBaseConfig {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private JdbcClient jdbcClient;

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiUtenti403() {
		//given
		//when
		ResponseEntity<UtenteListResponseDto> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.getForEntity("/api/utente", UtenteListResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiUtenti401() {
		//given
		//when
		ResponseEntity<UtenteListResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.getForEntity("/api/utente", UtenteListResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiInformazioneUtente403() {
		//given
		//when
		ResponseEntity<UtenteResponseDto> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.getForEntity("/api/utente/{id}/informazioni", UtenteResponseDto.class,"dffgdfgfgbfgbgbgbfgb454534");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiInformazioneUtente401() {
		//given
		//when
		ResponseEntity<UtenteResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.getForEntity("/api/utente/informazioni", UtenteResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}


}
