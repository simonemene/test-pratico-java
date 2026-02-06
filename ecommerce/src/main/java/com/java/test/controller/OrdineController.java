package com.java.test.controller;

import com.java.test.dto.OrdineEffettuatoResponseDto;
import com.java.test.dto.OrdineRequestDto;
import com.java.test.dto.OrdineResponseDto;
import com.java.test.dto.OrdiniResponseDto;
import com.java.test.service.IOrdineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api/ordine")
@RequiredArgsConstructor
@RestController
public class OrdineController {

	private final IOrdineService service;

	@PostMapping
	public ResponseEntity<OrdineEffettuatoResponseDto> effettuaOrdine(@RequestBody @Valid
			OrdineRequestDto ordineRequestDto)
	{
		OrdineEffettuatoResponseDto response = service.effettuaOrdine(ordineRequestDto.utenteId(),ordineRequestDto.prodotti());
		URI path = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.idPubblico())
				.toUri();
		return ResponseEntity.created(path).body(response);
	}

	@GetMapping
	public ResponseEntity<OrdiniResponseDto> prendiTuttiGliOrdini()
	{
		return ResponseEntity.ok(service.ricercaTuttiGliOrdini());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrdineResponseDto> prendiOrdine(@PathVariable @NotBlank(message = "L'id dell'ordine non può essere vuoto") String id)
	{
		return ResponseEntity.ok(service.ricercaOrdinePerId(id));
	}
}
