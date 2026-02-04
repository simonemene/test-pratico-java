package com.java.test.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdottoRequestDto(@NotNull @DecimalMin(value = "0.01", message = "Il prezzo deve essere uguale o maggiore di 0.01") BigDecimal prezzo,
								 @NotBlank(message = "Il nome del prodotto non può essere vuoto") String nome) {
}
