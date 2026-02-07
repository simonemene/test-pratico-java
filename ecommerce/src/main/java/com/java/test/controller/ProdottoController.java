package com.java.test.controller;

import com.java.test.dto.*;
import com.java.test.service.IProdottoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
	public ResponseEntity<ProdottoResponseDto> inserisciUnProdotto(@RequestBody @Valid
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
	public ResponseEntity<ProdottoConQuantitaResponseDto> prendiInformazioniProdotto(
			@PathVariable
	        @NotBlank(message = "L'id del prodotto non può essere vuoto") String id)
	{
		return ResponseEntity.ok(service.prendiInformazioniProdotto(id));
	}

	@GetMapping("/paginati")
	public ResponseEntity<PageResponseDto<ProdottoResponseDto>> prendiProdottiPaginati(Pageable pageable)
	{
		return ResponseEntity.ok(service.prendiProdottiPageable(pageable));
	}
}
