package com.java.test.service;

import com.java.test.entity.RuoloEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.enums.RuoloEnum;
import com.java.test.exception.MagazzinoException;
import com.java.test.exception.ProdottoException;
import com.java.test.exception.UtenteException;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.repository.OrdineRepository;
import com.java.test.repository.StockRepository;
import com.java.test.repository.UtenteRepository;
import com.java.test.service.impl.OrdineService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrdineServiceUnitTest {

	@InjectMocks
	private OrdineService service;

	@Mock
	private ProdottoMapper mapper;

	@Mock
	private StockRepository repository;

	@Mock
	private UtenteRepository utenteRepository;

	@Mock
	private OrdineRepository ordineRepository;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void erroreNessunProdotto()
	{

		//given
		//when
		//then
		Assertions.assertThatThrownBy(()->service.effettuaOrdine("12a", Map.of()))
				.isInstanceOf(ProdottoException.class)
				.hasMessageContaining("Nessun prodotto per l'ordine effettuato dall'utente");
	}

	@Test
	public void erroreRicercaUtente()
	{

		//given
		Mockito.when(utenteRepository.findByUtenteId(Mockito.anyString())).thenReturn(
				Optional.empty());
		//when
		//then
		Assertions.assertThatThrownBy(()->service.effettuaOrdine("12a", Map.of("prodotto1",2,"prodotto2",10)))
				.isInstanceOf(UtenteException.class)
				.hasMessageContaining("Nessun utente trovato");
	}

	@Test
	public void erroreMagazzinoModifica()
	{

		//given
		UtenteEntity utente = new UtenteEntity("prova@prova.it","Paolo","Rossi","fsdfgsdfsdfs","password",new RuoloEntity(RuoloEnum.ROLE_USER));
		Mockito.when(utenteRepository.findByEmail(Mockito.anyString())).thenReturn(
				Optional.of(utente));
		Mockito.when(repository.modificaQuantitaProdotto(Mockito.anyInt(),Mockito.anyString())).thenReturn(0);

		//when
		//then
		Assertions.assertThatThrownBy(()->service.effettuaOrdine("12a", Map.of("prodotto1",2,"prodotto2",10)))
				.isInstanceOf(MagazzinoException.class)
				.hasMessageContaining("Errore: il prodotto non ha giacenze in magazzino");
	}

	@Test
	public void erroreRicercaProdotti()
	{
		//given
		UtenteEntity utente = new UtenteEntity("prova@prova.it","Paolo","Rossi","fsdfgsdfsdfs","password",new RuoloEntity(RuoloEnum.ROLE_USER));
		Mockito.when(utenteRepository.findByEmail(Mockito.anyString())).thenReturn(
				Optional.of(utente));
		Mockito.when(repository.modificaQuantitaProdotto(Mockito.anyInt(),Mockito.anyString())).thenReturn(2);
		Mockito.when(repository.findByProdotto_ProductIdIn(Mockito.anyList())).thenReturn(
				List.of());
		//when
		//then
		Assertions.assertThatThrownBy(()->service.effettuaOrdine("12a", Map.of("prodotto1",2,"prodotto2",10)))
				.isInstanceOf(ProdottoException.class)
				.hasMessageContaining("Non riesco a trovare i prodotti per l'ordine nel sistema");
	}
}
