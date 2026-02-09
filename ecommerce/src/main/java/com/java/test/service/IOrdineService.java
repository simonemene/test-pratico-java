package com.java.test.service;

import com.java.test.dto.*;
import org.springframework.data.domain.Pageable;

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

	PageResponseDto<OrdineResponseDto> prendiOrdiniPaginati(Pageable pageable);

	PageResponseDto<OrdineResponseDto> prendiOrdiniPerUtentePaginati(String id,Pageable pageable);

	PageResponseDto<OrdineResponseDto> prendiOrdiniPerUtentePaginatiUtente(String id, Pageable pageable);

	OrdineResponseDto diminuisciQuantitaProdotto(String idOrdine, String idProdotto, int quantita);
}
