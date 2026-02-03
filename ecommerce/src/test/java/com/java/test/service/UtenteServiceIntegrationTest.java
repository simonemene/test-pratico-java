package com.java.test.service;

import com.java.test.TestjavaApplicationTests;
import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import com.java.test.repository.UtenteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
		UtenteDto utente = new UtenteDto("Paolo","Rossi","prova@prova.com","DRI456JHKGIT976S");
		//when
		UtenteDto utenteSalvato = service.creazioneUtente(utente);
		//then
		UtenteEntity utenteSalvatoEntity = repository.findByEmail(utenteSalvato.email());

		Assertions.assertThat(utenteSalvato).isNotNull();
		Assertions.assertThat(utenteSalvatoEntity.getId()).isGreaterThan(0L);
		Assertions.assertThat(utenteSalvato.nome()).isEqualTo(utenteSalvatoEntity.getNome());
		Assertions.assertThat(utenteSalvato.cognome()).isEqualTo(utenteSalvatoEntity.getCognome());
		Assertions.assertThat(utenteSalvato.codiceFiscale()).isEqualTo(utenteSalvatoEntity.getCodiceFiscale());
	}
}
