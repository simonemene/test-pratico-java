package com.java.test.service;

import com.java.test.dto.UtenteDto;

import java.util.List;

public interface IUtenteService {

	UtenteDto creazioneUtente(UtenteDto utente);

	List<UtenteDto> prendiListaClienti();


}
