package com.java.test.controller;

import com.java.test.dto.*;
import com.java.test.service.IProdottoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@Tag(
		name = "Controller Prodotti",
		description = "API per la gestione dei prodotti all'interno del negozio"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prodotto")
public class ProdottoController {

	private final IProdottoService service;


	@Operation(
			summary="Creazione prodotto",
			description = "API per la creazione di un prodotto"
	)
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

	@Operation(
			summary="Estrazione prodotti",
			description = "API per l'estrazione di tutti i prodotti"
	)
	@GetMapping
	public ResponseEntity<ProdottoListResponseDto> prendiProdotti()
	{
		return ResponseEntity.ok(service.prendiListaProdotti());
	}

	@Operation(
			summary="Informazioni prodotto",
			description = "API per l'estrazione di tutte le informazioni di uno specifico prodotto"
	)
	@GetMapping("/{id}")
	public ResponseEntity<ProdottoConQuantitaResponseDto> prendiInformazioniProdotto(
			@PathVariable
	        @NotBlank(message = "L'id del prodotto non può essere vuoto") String id)
	{
		return ResponseEntity.ok(service.prendiInformazioniProdotto(id));
	}

	@Operation(
			summary="Estrazione paginata prodotti",
			description = "API per l'estrazione paginata di tutti i prodotti"
	)
	@GetMapping("/paginati")
	public ResponseEntity<PageResponseDto<ProdottoResponseDto>> prendiProdottiPaginati(Pageable pageable)
	{
		return ResponseEntity.ok(service.prendiProdottiPageable(pageable));
	}
}
