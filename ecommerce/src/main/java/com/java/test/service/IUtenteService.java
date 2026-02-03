package com.java.test.service;

import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;

public interface IUtenteService {

	UtenteResponseDto creazioneUtente(UtenteRequestDto utente);

	UtenteListResponseDto prendiListaClienti();


}
