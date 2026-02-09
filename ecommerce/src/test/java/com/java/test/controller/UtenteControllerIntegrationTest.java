package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UtenteControllerIntegrationTest {

	@Autowired
	private JdbcClient jdbcClient;

	@Autowired
	private TestRestTemplate template;

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void creazioneUtente() throws JsonProcessingException {
		//given
		jdbcClient.sql("INSERT INTO RUOLO(RUOLO,RUOLO_ID) VALUES('ROLE_USER','USER')")
				.update();
		UtenteRequestDto request = new UtenteRequestDto("Paolo","Rossi","prova@prova.it","345TGFDCFGTR5462","passwordfsdfdsfdfdsds");
		//when
		ResponseEntity<UtenteResponseDto> response = template.postForEntity(
				"/api/utente",request,UtenteResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getHeaders().getLocation().getPath())
				.isEqualTo("/api/utente/"+response.getBody().utenteId());
		Assertions.assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("utenteId").isEqualTo(request);

	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiUtenti()
	{
		//given
		//when
		ResponseEntity<UtenteListResponseDto> response = template.getForEntity("/api/utente",UtenteListResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		UtenteListResponseDto body = response.getBody();
		Assertions.assertThat(body.utenti().size()).isEqualTo(3);
		UtenteResponseDto utentePaoloRossi = body.utenti().stream().filter(utente->utente.email().equals("prova@prova.com")).findFirst().get();
		Assertions.assertThat(utentePaoloRossi.nome()).isEqualTo("Paolo");
		Assertions.assertThat(utentePaoloRossi.cognome()).isEqualTo("Rossi");
		Assertions.assertThat(utentePaoloRossi.codiceFiscale()).isEqualTo("3454RFDFGBNHJUY3");
		Assertions.assertThat(utentePaoloRossi.utenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");
	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void prendiInformazioneUtente()
	{
		//given
		//when
		ResponseEntity<UtenteResponseDto> response = template.getForEntity("/api/utente/informazioni",UtenteResponseDto.class,"dffgdfdddddgbgbfgb454534");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		UtenteResponseDto utenteDaControllare = response.getBody();
		Assertions.assertThat(utenteDaControllare.nome()).isEqualTo("Marco");
		Assertions.assertThat(utenteDaControllare.cognome()).isEqualTo("Bianchi");
		Assertions.assertThat(utenteDaControllare.codiceFiscale()).isEqualTo("3454RFDFGBNHJUY2");
		Assertions.assertThat(utenteDaControllare.email()).isEqualTo("prova2@prova.com");
	}

}
