package com.java.test.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiminuisciProdottoOrdineRequestDto(@NotBlank(message = "Prodotto non valido")
                                                 String prodottoId,

                                                 @NotNull(message = "La quantità non può essere vuota")
                                                 @Min(value = 1, message = "La quantità da diminuire deve essere almeno 1")
                                                 @Max(value = 99, message = "Quantità troppo alta")
                                                 Integer quantita) {

}
