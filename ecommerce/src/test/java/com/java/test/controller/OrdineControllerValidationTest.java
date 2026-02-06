package com.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.configuration.SecurityConfiguration;
import com.java.test.dto.OrdineRequestDto;
import com.java.test.service.IOrdineService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@WebMvcTest(value = OrdineController.class)
@Import(SecurityConfiguration.class)
public class OrdineControllerValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private IOrdineService service;

	@Test
	public void controlloValidazioneOrdineRequest() throws Exception {
		//given
		OrdineRequestDto ordine = new OrdineRequestDto("",null);
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/ordine").content(
						objectMapper.writeValueAsString(ordine)
				).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value("/api/ordine"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.utenteId").value("l'utente non può essere vuoto"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.prodotti").value(
						Matchers.allOf(Matchers.containsString(
						"L'elenco dei prodotti non può essere vuoto"),
								Matchers.containsString("Elenco dei prodotto deve esistere"))
				));
	}

	@Test
	public void controlloValidazioneOrdineValoreQuantitaRequest() throws Exception {
		//given
		OrdineRequestDto ordine = new OrdineRequestDto("", Map.of("prodotto",0));
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/ordine").content(
						objectMapper.writeValueAsString(ordine)
				).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value("/api/ordine"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.utenteId").value("l'utente non può essere vuoto"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori['prodotti[prodotto]']").value(
						"La quantità di prodotto non può essere minore di 0"));
	}
}
