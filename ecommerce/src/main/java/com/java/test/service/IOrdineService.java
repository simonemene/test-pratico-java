package com.java.test.service;

import com.java.test.dto.OrdineEffettuatoResponseDto;

import java.util.Map;

public interface IOrdineService {

	OrdineEffettuatoResponseDto effettuaOrdine(String utenteId, Map<String,Integer> prodotti);
}
