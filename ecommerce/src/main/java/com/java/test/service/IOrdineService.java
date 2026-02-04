package com.java.test.service;

import com.java.test.dto.OrdineEffettuatoResponseDto;

public interface IOrdineService {

	OrdineEffettuatoResponseDto effettuaOrdine(String utenteId,String prodottoId, int quantita);
}
