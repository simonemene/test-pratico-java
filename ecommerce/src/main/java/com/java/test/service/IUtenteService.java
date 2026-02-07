package com.java.test.service;

import com.java.test.dto.PageResponseDto;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import org.springframework.data.domain.Pageable;

public interface IUtenteService {

	UtenteResponseDto creazioneUtente(UtenteRequestDto utente);

	UtenteListResponseDto prendiListaClienti();

	UtenteResponseDto prendiInformazionUtente(String idPubblico);

	PageResponseDto<UtenteResponseDto> prendiUtentiPaginati(Pageable pageable);


}
