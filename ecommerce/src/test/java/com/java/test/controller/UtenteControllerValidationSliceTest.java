package com.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.configuration.JpaConfiguration;
import com.java.test.configuration.SecurityConfiguration;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.service.IUtenteService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = UtenteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UtenteControllerValidationSliceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private IUtenteService service;

	@Test
	public void controlloValidazioneUtenteRequest() throws Exception {
		//given
		UtenteRequestDto request = new UtenteRequestDto("","","","345TGFDCFGTR546","passwordffdsfdsfsdffdfds");
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utente").content(
				objectMapper.writeValueAsString(request)
		).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value("/api/utente"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.nome").value("Il nome non deve essere vuoto"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.cognome").value("Il cognome non deve essere vuoto"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.email")
						.value("La e-mail non deve essere vuota"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.codiceFiscale").value("Il codice fiscale deve essere lungo 16"));
	}

	@Test
	public void controlloValidazioneUtenteRequestEmailErroreFormato() throws Exception {
		//given
		UtenteRequestDto request = new UtenteRequestDto("Paolo","Rossi","prova","345TGFDCFGTR5462","passwordfsdfsdfdsfdsfsfsdf");
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utente").content(
						objectMapper.writeValueAsString(request)
				).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value("/api/utente"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.email")
						.value("La mail non rispetta i parametri"));
	}

	@Test
	public void controlloValidazioneUtenteInformazioni() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/utente/{id}/informazioni"," ")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value(
						Matchers.startsWith("/api/utente")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errori.id").value("L'id dell'utente non può essere vuoto"));
	}
}
