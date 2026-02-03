package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UtenteControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void creazioneUtente() throws JsonProcessingException {
		//given
		UtenteRequestDto request = new UtenteRequestDto("Paolo","Rossi","prova@prova.it","345TGFDCFGTR5462");
		//when
		ResponseEntity<UtenteResponseDto> response = template.postForEntity(
				"/api/utente",request,UtenteResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getHeaders().getLocation().getPath())
				.isEqualTo("/api/utente/"+response.getBody().utenteId());
		Assertions.assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("utenteId").isEqualTo(request);

	}

}
