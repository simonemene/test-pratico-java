package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

@AutoConfigureTestDatabase
public class OrdineSecurityServiceIntegrationTest  extends TestjavaApplicationTests {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private OrdineRepository ordineRepository;

	@Autowired
	private IOrdineService service;

	@Autowired
	private JdbcClient jdbcClient;

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void ricercaOrdinePerIdNonAppartieneOrdineNoAutorizzato() {
		//given
		String ordineId = "321KL";
		//when
		//then
		Assertions.assertThatThrownBy(()->service.ricercaOrdinePerId(ordineId))
				.isInstanceOf(AuthorizationDeniedException.class)
				.hasMessageContaining("Access Denied");
	}

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void modificaNumeriProdottoNonAutorizzato() {
		//given
		String id = "321KL";
		Map<String, Integer> prodotti = Map.of("dgfgdfgdf45mnbv", 2);
		//when
		//then
		Assertions.assertThatThrownBy(()->service.modificaProdotti(id, prodotti))
				.isInstanceOf(AuthorizationDeniedException.class)
				.hasMessageContaining("Access Denied");

	}

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void eliminaProdottiDaOrdineNonAutorizzato() {
		//given
		String id = "321KL";
		List<String> prodotti = List.of("dgfgdfgdf45mnbv");
		//when
		//then
		Assertions.assertThatThrownBy(()->service.eliminaProdotti(id, prodotti))
				.isInstanceOf(AuthorizationDeniedException.class)
				.hasMessageContaining("Access Denied");
	}

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void inserisciProdottiDaOrdineNonAutorizzato() {
		//given
		String id = "321KL";
		Map<String, Integer> prodotti = Map.of("rgvbdfgdf454345", 2);
		Assertions.assertThat(jdbcClient.sql(
						"SELECT COUNT(*) FROM MOVIMENTO WHERE " + "  ID_ORDINE = (SELECT ID FROM ORDINE WHERE ORDINE_ID=?)")
				.param(1, id).query().singleValue()).isEqualTo(1L);
		//when
		//then
		Assertions.assertThatThrownBy(()->service.inserisciProdotti(id, prodotti))
				.isInstanceOf(AuthorizationDeniedException.class)
				.hasMessageContaining("Access Denied");

	}

	@WithMockUser(username = "prova1@prova.com",roles = "USER")
	@Sql(scripts = "classpath:sql/service/ordini/ordine-sicurezza.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:sql/service/delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Test
	public void cancellaOrdineNonAutorizzato() {
		//given
		String id = "321KL";
		//when
		//then
		Assertions.assertThatThrownBy(()->service.cancellazioneOrdine(id))
				.isInstanceOf(AuthorizationDeniedException.class)
				.hasMessageContaining("Access Denied");
	}
}
