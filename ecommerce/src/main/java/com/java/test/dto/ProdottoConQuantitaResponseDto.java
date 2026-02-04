package com.java.test.dto;

import java.math.BigDecimal;

public record ProdottoConQuantitaResponseDto(BigDecimal prezzo, String nome, String productId, int quantita) {
}
