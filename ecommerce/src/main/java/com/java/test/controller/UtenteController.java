package com.java.test.controller;

import com.java.test.dto.UtenteDto;
import com.java.test.service.IUtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/utente")
public class UtenteController {

	private final IUtenteService service;

	@PostMapping
	public ResponseEntity<UtenteDto> creazioneUtente(@RequestBody UtenteDto utenteDto)
	{
		UtenteDto utenteCreato = service.creazioneUtente(utenteDto);
		URI path = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/{id}")
				.buildAndExpand(utenteCreato.utenteId())
				.toUri();
		return ResponseEntity.created(path).body(utenteCreato);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UtenteDto> prendiInformazioniUtente(@PathVariable String id)
	{
		return null;
	}
}
