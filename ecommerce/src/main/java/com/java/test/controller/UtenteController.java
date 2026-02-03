package com.java.test.controller;

import com.java.test.dto.UtenteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utente")
public class UtenteController {

	@PostMapping
	public ResponseEntity<UtenteDto> creazioneUtente(@RequestBody UtenteDto utenteDto)
	{
return null;
	}
}
