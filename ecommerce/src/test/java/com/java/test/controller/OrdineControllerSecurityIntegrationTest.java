package com.java.test.controller;

import com.java.test.dto.OrdineRequestDto;
import com.java.test.dto.OrdineResponseDto;
import com.java.test.dto.OrdiniCancellatiRequestDto;
import com.java.test.dto.PageResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrdineControllerSecurityIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private JdbcClient jdbcClient;

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void modificaOrdine401() {
		//given
		OrdineRequestDto request = new OrdineRequestDto("564W",
				Map.of("dgfgdfgdf45mnbv", 2));
		String id = "564W";

		//when
		ResponseEntity<OrdineResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.exchange("/api/ordine/{id}/prodotti", HttpMethod.PUT,
						new HttpEntity<>(request), OrdineResponseDto.class, id);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		Assertions.assertThat(response.getBody()).isEqualTo(new OrdineResponseDto(null,null,null));
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void cancellaProdottiOrdine401() {
		//given
		OrdiniCancellatiRequestDto prodotti = new OrdiniCancellatiRequestDto(
				List.of("dgfgdfgdf45mnbv"));
		String id = "564W";
		Assertions.assertThat(jdbcClient.sql(
								"SELECT FLG_ANNULLO FROM MOVIMENTO WHERE " + "ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" + " AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1, "dgfgdfgdf45mnbv").param(2, id).query().singleValue())
				.isEqualTo("N");
		//when
		ResponseEntity<Void> response = template.withBasicAuth("no-auth@prova.com", "prova")
				.exchange("/api/ordine/{id}/prodotti", HttpMethod.DELETE,
						new HttpEntity<>(prodotti), Void.class, id);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void inserisciProdottoOrdine401() {
		//given
		String id = "564W";
		OrdineRequestDto ordine = new OrdineRequestDto("dffgdfgfgbfgbgbgbfgb454534",
				Map.of("rgvbdfgdf454345", 3));
		Assertions.assertThat(jdbcClient.sql("""
				SELECT COUNT(*) FROM MOVIMENTO AS M JOIN ORDINE AS O 
				ON O.ID = M.ID_ORDINE
				WHERE O.ORDINE_ID = ?
				""").param(1, id).query().singleValue()).isEqualTo(2L);
		//when
		ResponseEntity<OrdineResponseDto> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.postForEntity("/api/ordine/{id}/prodotti", ordine,
						OrdineResponseDto.class, id);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void cancellaOrdine401() {
		//given
		String id = "564W";
		Assertions.assertThat(jdbcClient.sql("""
				SELECT FLG_ANNULLO FROM ORDINE WHERE ORDINE_ID = ?
				""").param(1, id).query().singleValue()).isEqualTo("N");
		List<String> flgMovimenti = jdbcClient.sql("""
				SELECT M.FLG_ANNULLO FROM MOVIMENTO AS M JOIN ORDINE AS O ON O.ID =M.ID_ORDINE
				WHERE O.ORDINE_ID = ?
				""").param(1, id).query(String.class).list();
		Assertions.assertThat(flgMovimenti).containsOnly("N");

		//when
		ResponseEntity<Void> response = template.withBasicAuth("no-auth@prova.com", "prova")
				.exchange("/api/ordine/{id}", HttpMethod.DELETE, null, Void.class, id);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableDefault401() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.exchange("/api/ordine/paginati", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						});
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableDefault403() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.exchange("/api/ordine/paginati", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						});
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableParametri401() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.exchange("/api/ordine/{id}/paginati?page=2&size=2", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						}, "UTENTE_1");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableParametri403() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.exchange("/api/ordine/{id}/paginati?page=2&size=2", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						}, "UTENTE_1");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPerUtentePageable401() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"no-auth@prova.com", "prova")
				.exchange("/api/ordine/{id}/paginati", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						}, "UTENTE_1");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPerUtentePageable403() {
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response = template.withBasicAuth(
						"prova@prova.com", "prova")
				.exchange("/api/ordine/{id}/paginati", HttpMethod.GET, null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {
						}, "UTENTE_1");
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

	}

}
