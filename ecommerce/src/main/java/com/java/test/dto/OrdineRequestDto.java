package com.java.test.dto;

import java.util.Map;

public record OrdineRequestDto(String utenteId, Map<String,String> prodotti) {
}
