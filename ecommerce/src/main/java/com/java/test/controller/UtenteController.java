package com.java.test.controller;

import com.java.test.annotation.CurrentUser;
import com.java.test.dto.PageResponseDto;
import com.java.test.dto.UtenteListResponseDto;
import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.service.IUtenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

	@GetMapping("/informazioni")
	public ResponseEntity<UtenteResponseDto> prendiInformazioniUtente(@CurrentUser String id)
	{
		return ResponseEntity.ok(service.prendiInformazionUtente(id));
	}

	@GetMapping("/{id}/informazioni")
	public ResponseEntity<UtenteResponseDto> prendiInformazioniUtenteId(@NotBlank(message = "L'id dell'utente non può essere vuoto") @PathVariable String id)
	{
		return ResponseEntity.ok(service.prendiInformazionUtente(id));
	}

	@GetMapping
	public ResponseEntity<UtenteListResponseDto> prendiUtenti()
	{
		return ResponseEntity.ok(service.prendiListaClienti());
	}

	@GetMapping("/paginati")
	public ResponseEntity<PageResponseDto<UtenteResponseDto>> prendiUtentiPaginati(Pageable pageable)
	{
		return ResponseEntity.ok(service.prendiUtentiPaginati(pageable));
	}
}
