package com.java.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.test.baseconfig.ControllerBaseConfig;
import com.java.test.dto.*;
import com.java.test.repository.OrdineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

public class OrdineControllerIntegrationTest extends ControllerBaseConfig {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private OrdineRepository repository;

	@Autowired
	private JdbcClient jdbcClient;


	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void creazioneOrdine() throws JsonProcessingException {
		//given
		OrdineRequestDto request = new OrdineRequestDto("dffgdfgfgbfgbgbgbfgb454534",
				Map.of("dgfgdwwdf454345",2,"rgvbdfgdf454345",1));
		//when
		ResponseEntity<OrdineEffettuatoResponseDto> response = template
				.withBasicAuth("prova1@prova.com","prova")
				.postForEntity(
				"/api/ordine",request, OrdineEffettuatoResponseDto.class);
		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getHeaders().getLocation().getPath())
				.isEqualTo("/api/ordine/"+response.getBody().idPubblico());
		Assertions.assertThat(repository.findByOrdineId(response.getBody().idPubblico()).get()).isNotNull();

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void modificaOrdine()
	{
		//given
		OrdineRequestDto request = new OrdineRequestDto("564W",
				Map.of("dgfgdfgdf45mnbv",2));
		String id = "564W";

		//when
		ResponseEntity<OrdineResponseDto> response =
				template
						.withBasicAuth("prova1@prova.com","prova")
						.exchange(
						"/api/ordine/{id}/prodotti",
						HttpMethod.PATCH,
						new HttpEntity<>(request),
						OrdineResponseDto.class,
						id
				);
		//then
		Assertions.assertThat(response.getBody().idPubblicoOrdine()).isEqualTo(id);
		List<MovimentoResponseDto> movimenti = response.getBody().movimenti();
		Assertions.assertThat(movimenti.size()).isEqualTo(2);
		MovimentoResponseDto movimento = movimenti.stream().filter(mov->mov.nomeProdotto().equals("COMPUTER")).findFirst().get();
		Assertions.assertThat(movimento.quantitaOrdinata()).isEqualTo(12);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void cancellaProdottiOrdine()
	{
         //given
		OrdiniCancellatiRequestDto prodotti = new OrdiniCancellatiRequestDto(List.of("dgfgdfgdf45mnbv"));
		String id = "564W";
		Assertions.assertThat(jdbcClient.sql("SELECT FLG_ANNULLO FROM MOVIMENTO WHERE " +
						"ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" +
						" AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
				.param(1,"dgfgdfgdf45mnbv")
				.param(2,id)
				.query().singleValue())
				.isEqualTo("N");
		//when
		template
				.withBasicAuth("prova1@prova.com","prova")
				.exchange(
						"/api/ordine/{id}/prodotti",
						HttpMethod.DELETE,
						new HttpEntity<>(prodotti),
						Void.class,
						id
		);
		//then
		Assertions.assertThat(jdbcClient.sql("SELECT FLG_ANNULLO FROM MOVIMENTO WHERE " +
								"ID_PRODOTTO = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)" +
								" AND ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
						.param(1,"dgfgdfgdf45mnbv")
						.param(2,id)
						.query().singleValue())
				.isEqualTo("S");
		Assertions.assertThat(jdbcClient.sql("SELECT QUANTITA FROM STOCK WHERE " +
								"PRODOTTO_ID = (SELECT ID FROM PRODOTTO WHERE ID_PUBBLICO_PRODOTTO =?)")
						.param(1,"dgfgdfgdf45mnbv")
						.query().singleValue())
				.isEqualTo(22);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void inserisciProdottoOrdine()
	{
         //given
		String id = "564W";
		OrdineRequestDto ordine = new OrdineRequestDto("dffgdfgfgbfgbgbgbfgb454534",
				Map.of("rgvbdfgdf454345",3));
		Assertions.assertThat(jdbcClient.sql(
				"""
						SELECT COUNT(*) FROM MOVIMENTO AS M JOIN ORDINE AS O 
						ON O.ID = M.ID_ORDINE
						WHERE O.ORDINE_ID = ?
						"""
		).param(1,id).query().singleValue()).isEqualTo(2L);
		//when
		ResponseEntity<OrdineResponseDto> response = template
				.withBasicAuth("prova1@prova.com","prova")
				.postForEntity(
				"/api/ordine/{id}/prodotti",ordine, OrdineResponseDto.class,id);
		//then
		Assertions.assertThat(jdbcClient.sql(
				"""
						SELECT COUNT(*) FROM MOVIMENTO AS M JOIN ORDINE AS O 
						ON O.ID = M.ID_ORDINE
						WHERE O.ORDINE_ID = ?
						"""
		).param(1,id).query().singleValue()).isEqualTo(3L);
		Assertions.assertThat(jdbcClient.sql(
				"""
						SELECT QUANTITA FROM STOCK AS S JOIN PRODOTTO AS P
						ON P.ID = S.PRODOTTO_ID
						WHERE P.ID_PUBBLICO_PRODOTTO = ?
						"""
		).param(1,"rgvbdfgdf454345")
				.query().singleValue()).isEqualTo(2);
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-completo.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void cancellaOrdine()
	{
		//given
		String id = "564W";
		Assertions.assertThat(jdbcClient.sql("""
								SELECT FLG_ANNULLO FROM ORDINE WHERE ORDINE_ID = ?
								""")
						.param(1,id)
						.query().singleValue())
				.isEqualTo("N");
		List<String> flgMovimenti = jdbcClient.sql("""
				SELECT M.FLG_ANNULLO FROM MOVIMENTO AS M JOIN ORDINE AS O ON O.ID =M.ID_ORDINE
				WHERE O.ORDINE_ID = ?
				""").param(1,id)
				.query(String.class)
						.list();
		Assertions.assertThat(flgMovimenti).containsOnly("N");

		//when
		template
				.withBasicAuth("prova1@prova.com","prova")
				.exchange(
				"/api/ordine/{id}",
				HttpMethod.DELETE,
				null,
				Void.class,
				id
		);
		//then
		Assertions.assertThat(jdbcClient.sql("""
								SELECT FLG_ANNULLO FROM ORDINE WHERE ORDINE_ID = ?
								""")
						.param(1,id)
						.query().singleValue())
				.isEqualTo("S");
		List<String> flgMovimentiModificato = jdbcClient.sql("""
				SELECT M.FLG_ANNULLO FROM MOVIMENTO AS M JOIN ORDINE AS O ON O.ID =M.ID_ORDINE
				WHERE O.ORDINE_ID = ?
				""").param(1,id)
				.query(String.class)
				.list();
		Assertions.assertThat(flgMovimentiModificato).containsOnly("S");
	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-paginato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableDefault()
	{
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response =
				template
						.withBasicAuth("admin@prova.com","prova")
						.exchange(
						"/api/ordine/paginati",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {}
				);
		//then
		Assertions.assertThat(response.getBody().contenuto().size()).isEqualTo(20);
		PageResponseDto<OrdineResponseDto> page = response.getBody();
		Assertions.assertThat(page.elementiTotali()).isEqualTo(22L);
		Assertions.assertThat(page.pagina()).isEqualTo(0);
		Assertions.assertThat(page.pagineTotali()).isEqualTo(2);
		Assertions.assertThat(page.dimensione()).isEqualTo(20);

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-paginato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPageableParametri()
	{
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response =
				template
						.withBasicAuth("admin@prova.com","prova")
						.exchange(
						"/api/ordine/{id}/paginati?page=2&size=2",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {},
						"UTENTE_1"
				);
		//then
		Assertions.assertThat(response.getBody().contenuto().size()).isEqualTo(2);
		PageResponseDto<OrdineResponseDto> page = response.getBody();
		Assertions.assertThat(page.elementiTotali()).isEqualTo(8L);
		Assertions.assertThat(page.pagina()).isEqualTo(2);
		Assertions.assertThat(page.pagineTotali()).isEqualTo(4);
		Assertions.assertThat(page.dimensione()).isEqualTo(2L);

		Assertions.assertThat(page.contenuto().stream().filter(p->p.idPubblicoOrdine().equals("ORD-10"))).isNotNull();
		Assertions.assertThat(page.contenuto().stream().filter(p->p.idPubblicoOrdine().equals("ORD-07"))).isNotNull();

	}

	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/ordini/insert-ordine-paginato.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	public void prendiOrdiniPerUtentePageable()
	{
		//given
		//when
		ResponseEntity<PageResponseDto<OrdineResponseDto>> response =
				template
						.withBasicAuth("admin@prova.com","prova")
						.exchange(
						"/api/ordine/{id}/paginati",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<PageResponseDto<OrdineResponseDto>>() {},
						"UTENTE_1"
				);
		//then
		Assertions.assertThat(response.getBody().contenuto().size()).isEqualTo(8);
		PageResponseDto<OrdineResponseDto> page = response.getBody();
		Assertions.assertThat(page.elementiTotali()).isEqualTo(8L);
		Assertions.assertThat(page.pagina()).isEqualTo(0);
		Assertions.assertThat(page.pagineTotali()).isEqualTo(1);
		Assertions.assertThat(page.dimensione()).isEqualTo(20L);
	}


}
