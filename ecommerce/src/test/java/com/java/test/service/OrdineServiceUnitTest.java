package com.java.test.service;

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
	public void erroreRicercaUtente()
	{

		//given
		Mockito.when(utenteRepository.findByUtenteId(Mockito.anyString())).thenReturn(
				Optional.empty());
		//when
		//then
		Assertions.assertThatThrownBy(()->service.effettuaOrdine("12a", Map.of("prodotto1",2,"prodotto2",10)))
				.isInstanceOf(UtenteException.class)
				.hasMessageContaining("Nessun utente trovato per id: 12a");
	}
}
