package com.java.test.repository;

import com.java.test.entity.UtenteEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class UtenteRepoitorySliceTest {

	@Autowired
	private UtenteRepository repository;

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
}
