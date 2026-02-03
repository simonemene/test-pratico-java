package com.java.test.service;

import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;

import java.util.List;

public interface IUtenteService {

	UtenteResponseDto creazioneUtente(UtenteRequestDto utente);

	List<UtenteResponseDto> prendiListaClienti();


}
