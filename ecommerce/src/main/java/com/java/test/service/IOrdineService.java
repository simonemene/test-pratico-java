package com.java.test.service;

import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.OrdineEliminatiProdottiResponseDto;
import com.java.test.dto.OrdineResponseDto;
import com.java.test.dto.OrdiniResponseDto;

import java.util.List;
import java.util.Map;

public interface IOrdineService {

	OrdineEffettuatoResponseDto effettuaOrdine(String utenteId, Map<String,Integer> prodotti);

	OrdineResponseDto ricercaOrdinePerId(String id);

	OrdiniResponseDto ricercaTuttiGliOrdini();

	OrdineResponseDto modificaProdotti(String id,Map<String,Integer> prodotti);

	OrdineEliminatiProdottiResponseDto eliminaProdotti(String id, List<String> idPubbliciProdotti);

	OrdineResponseDto inserisciProdotti(String id,Map<String,Integer> prodotti);

	String cancellazioneOrdine(String id);
}
