package com.java.test.controller;

import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.service.IUtenteService;
import jakarta.validation.Valid;
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
	public ResponseEntity<UtenteResponseDto> creazioneUtente(@RequestBody @Valid UtenteRequestDto utenteDto)
	{
		UtenteResponseDto utenteCreato = service.creazioneUtente(utenteDto);
		URI path = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(utenteCreato.utenteId())
				.toUri();
		return ResponseEntity.created(path).body(utenteCreato);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UtenteResponseDto> prendiInformazioniUtente(@PathVariable String id)
	{
		return null;
	}

	@GetMapping
	public ResponseEntity<UtenteListResponseDto> prendiUtenti()
	{
		return ResponseEntity.ok(service.prendiListaClienti());
	}
}
