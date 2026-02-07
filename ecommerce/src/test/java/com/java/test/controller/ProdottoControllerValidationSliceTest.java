package com.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.configuration.SecurityConfiguration;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.service.IProdottoService;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@WebMvcTest(value = ProdottoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProdottoControllerValidationSliceTest {


		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private ObjectMapper objectMapper;

		@MockitoBean
		private IProdottoService service;

		@Test
		public void controlloValidazioneProdottoRequest() throws Exception {
			//given
			ProdottoRequestDto request = new ProdottoRequestDto(new BigDecimal("0.00"),"");
			//when
			//then
			mockMvc.perform(MockMvcRequestBuilders.post("/api/prodotto").content(
							objectMapper.writeValueAsString(request)
					).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value("/api/prodotto"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(2))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errori.prezzo").value("Il prezzo deve essere uguale o maggiore di 0.01"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errori.nome").value("Il nome del prodotto non può essere vuoto"));
		}

		@Test
		public void controlloValidazioneProdottoInformazioni() throws Exception {
			//given
			//when
			//then
			mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotto/{id}"," ")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Errore nella validazione dei campi"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Uno o più campi sono invalidi"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.instance").value(
							Matchers.startsWith("/api/prodotto")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errori.length()").value(1))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errori.id").value("L'id del prodotto non può essere vuoto"));
		}


}
