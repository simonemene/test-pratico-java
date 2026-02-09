package com.java.test.dto;

import jakarta.validation.constraints.*;

import java.util.Map;

public record OrdineRequestDto(
		@NotBlank(message = "l'ordine non può essere vuoto")
		String ordineId,
		@NotNull(message = "Elenco dei prodotto deve esistere")
		@NotEmpty(message = "L'elenco dei prodotti non può essere vuoto")
		Map<@NotBlank(message = "Prodotto non valido") String,@Min(value = 1,message = "La quantità di prodotto non può essere minore di 0")
				@Max(value = 99,message = "Troppi prodotti ordinati") Integer> prodotti) {
}
