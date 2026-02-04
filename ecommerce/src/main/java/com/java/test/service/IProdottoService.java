package com.java.test.service;

import com.java.test.dto.ProdottoConQuantitaResponseDto;
import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;

public interface IProdottoService {

	ProdottoResponseDto inserisciUnProdotto(ProdottoRequestDto prodottoDaInserire);

	ProdottoListResponseDto prendiListaProdotti();

	ProdottoConQuantitaResponseDto prendiInformazioniProdotto(String productId);
}
