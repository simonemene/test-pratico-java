package com.java.test.dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> contenuto,
        int pagine,
        int dimensione,
        int elementiTotali,
        int pagineTotali
) {
}
