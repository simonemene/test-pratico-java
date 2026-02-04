package com.java.test.controller;

import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import com.java.test.service.IProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prodotto")
public class ProdottoController {

	private final IProdottoService service;


	@PostMapping
	public ResponseEntity<ProdottoResponseDto> inserisciUnProdotto(@RequestBody
			ProdottoRequestDto prodotto)
	{
		ProdottoResponseDto prodottoCreato = service.inserisciUnProdotto(prodotto);
		URI path = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(prodottoCreato.productId())
				.toUri();
		return ResponseEntity.created(path).body(prodottoCreato);
	}

	@GetMapping
	public ResponseEntity<ProdottoListResponseDto> prendiProdotti()
	{
		return ResponseEntity.ok(service.prendiListaProdotti());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdottoResponseDto> prendiInformazioniProdotto(@PathVariable String id)
	{
		return ResponseEntity.ok(service.prendiInformazioniProdotto(id));
	}
}
