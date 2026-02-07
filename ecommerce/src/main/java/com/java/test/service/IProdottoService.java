package com.java.test.service;

import com.java.test.dto.*;
import org.springframework.data.domain.Pageable;

public interface IProdottoService {

	ProdottoResponseDto inserisciUnProdotto(ProdottoRequestDto prodottoDaInserire);

	ProdottoListResponseDto prendiListaProdotti();

	ProdottoConQuantitaResponseDto prendiInformazioniProdotto(String productId);

	PageResponseDto<ProdottoResponseDto> prendiProdottiPageable(Pageable pageable);
}
