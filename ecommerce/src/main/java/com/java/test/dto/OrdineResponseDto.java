package com.java.test.dto;

import java.util.List;

public record OrdineResponseDto(String idPubblicoOrdine, List<MovimentoResponseDto> movimenti, String utenteId) {
}
