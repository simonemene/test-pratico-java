package com.java.test.service;

import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;

public interface IProdottoService {

	ProdottoResponseDto inserisciUnProdotto(ProdottoRequestDto prodottoDaInserire);
}
