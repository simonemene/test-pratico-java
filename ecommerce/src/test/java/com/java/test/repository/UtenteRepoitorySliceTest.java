package com.java.test.repository;

import com.java.test.entity.UtenteEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase
public class UtenteRepoitorySliceTest {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private JdbcClient jdbcClient;

	@Test
	public void trovaUtenteTramiteEmail()
	{
		//given
		UtenteEntity utente = new UtenteEntity("prova@prova.com","Paolo","Rossi","DFRTGFV4563EDSFR");
		repository.save(utente);
		//when
		UtenteEntity utenteTrovato = repository.findByEmail(utente.getEmail());
		//then
		Assertions.assertThat(utenteTrovato).isNotNull();
		Assertions.assertThat(utenteTrovato.getNome()).isEqualTo(utente.getNome());
		Assertions.assertThat(utenteTrovato.getCognome()).isEqualTo(utente.getCognome());
		Assertions.assertThat(utenteTrovato.getCodiceFiscale()).isEqualTo(utente.getCodiceFiscale());
		Assertions.assertThat(utenteTrovato.getOrdine().size()).isEqualTo(0);
	}

	@Test
	public void trovaUtenteTramiteIdPubblico()
	{
		//given
		jdbcClient.sql("INSERT INTO UTENTE(EMAIL,NOME,COGNOME,CODICE_FISCALE,UTENTE_ID) VALUES" +
				"(?,?,?,?,?)")
				.param(1,"prova@prova.com")
				.param(2,"Marco")
				.param(3,"Rossi")
				.param(4,"345RTGFHYUJHNBBF")
				.param(5,"12A")
				.update();

		//when
		UtenteEntity utenteTrovato = repository.findByEmail("prova@prova.com");
		//then
		Assertions.assertThat(utenteTrovato).isNotNull();
		Assertions.assertThat(utenteTrovato.getNome()).isEqualTo("Marco");
		Assertions.assertThat(utenteTrovato.getCognome()).isEqualTo("Rossi");
		Assertions.assertThat(utenteTrovato.getCodiceFiscale()).isEqualTo("345RTGFHYUJHNBBF");
		Assertions.assertThat(utenteTrovato.getUtenteId()).isEqualTo("12A");
		Assertions.assertThat(utenteTrovato.getOrdine().size()).isEqualTo(0);
	}
}
