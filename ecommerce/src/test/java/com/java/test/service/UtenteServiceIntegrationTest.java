package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureTestDatabase
public class UtenteServiceIntegrationTest extends TestjavaApplicationTests {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private IUtenteService service;


	@Test
	public void inserisciUtente()
	{
		//given
		UtenteRequestDto utente = new UtenteRequestDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S");
		//when
		UtenteResponseDto utenteSalvato = service.creazioneUtente(utente);
		//then
		UtenteEntity utenteSalvatoEntity = repository.findByEmail(utenteSalvato.email());

		Assertions.assertThat(utenteSalvato).isNotNull();
		Assertions.assertThat(utenteSalvatoEntity.getId()).isGreaterThan(0L);
		Assertions.assertThat(utenteSalvato.nome()).isEqualTo(utenteSalvatoEntity.getNome());
		Assertions.assertThat(utenteSalvato.cognome()).isEqualTo(utenteSalvatoEntity.getCognome());
		Assertions.assertThat(utenteSalvato.codiceFiscale()).isEqualTo(utenteSalvatoEntity.getCodiceFiscale());
	}

	@Sql(scripts = "classpath:sql/service/clienti/insert-utenti.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	 ,config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.INFERRED))
	@Test
	public void prendiTuttiGliUtenti()
	{
		//given
		//when
		UtenteListResponseDto utenti = service.prendiListaClienti();
		//then
		Assertions.assertThat(utenti.utenti().size()).isEqualTo(3);
		UtenteResponseDto utentePaoloRossi = utenti.utenti().stream().filter(utente->utente.email().equals("prova@prova.com")).findFirst().get();
		Assertions.assertThat(utentePaoloRossi.nome()).isEqualTo("Paolo");
		Assertions.assertThat(utentePaoloRossi.cognome()).isEqualTo("Rossi");
		Assertions.assertThat(utentePaoloRossi.codiceFiscale()).isEqualTo("3454RFDFGBNHJUY3");
		Assertions.assertThat(utentePaoloRossi.utenteId()).isEqualTo("dffgdfgfgbfgbgbgbfgb454534");
	}
}
