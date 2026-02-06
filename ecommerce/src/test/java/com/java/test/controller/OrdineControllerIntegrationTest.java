package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.OrdineRequestDto;
import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import com.java.test.entity.StockEntity;
import com.java.test.repository.OrdineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrdineControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private OrdineRepository repository;

	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void creazioneOrdine() throws JsonProcessingException {
		//given
		OrdineRequestDto request = new OrdineRequestDto("dffgdfgfgbfgbgbgbfgb454534",
				Map.of("dgfgdwwdf454345",2,"rgvbdfgdf454345",1));
		//when
		ResponseEntity<OrdineEffettuatoResponseDto> response = template.postForEntity(
				"/api/ordine",request, OrdineEffettuatoResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getHeaders().getLocation().getPath())
				.isEqualTo("/api/ordine/"+response.getBody().idPubblico());
		Assertions.assertThat(repository.findByOrdineId(response.getBody().idPubblico()).get()).isNotNull();

	}
}
