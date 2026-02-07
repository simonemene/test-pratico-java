package com.java.test.dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> contenuto,
        int pagina,
        int dimensione,
        long elementiTotali,
        int pagineTotali
) {
}
