package com.java.test.controller;

import com.java.test.dto.*;
import com.java.test.service.IOrdineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

	@PutMapping("/{id}/prodotti")
	public ResponseEntity<OrdineResponseDto> modificaProdotti(@PathVariable String id,@RequestBody OrdineRequestDto prodottiModificati)
	{
		return ResponseEntity.ok(service.modificaProdotti(id,prodottiModificati.prodotti()));
	}

	@DeleteMapping("/{id}/prodotti")
	public ResponseEntity<Void> eliminaProdotti(@PathVariable String id,@RequestBody OrdiniCancellatiRequestDto prodottiEliminati)
	{
		service.eliminaProdotti(id,prodottiEliminati.prodotti());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/{id}/prodotti")
	public ResponseEntity<OrdineResponseDto> inserisciProdotti(@PathVariable String id,@RequestBody OrdineRequestDto inserimentoProdotti)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(service.inserisciProdotti(id,inserimentoProdotti.prodotti()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaOrdine(@PathVariable String id)
	{
		service.cancellazioneOrdine(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/paginati")
	public ResponseEntity<PageResponseDto<OrdineResponseDto>> prendiOrdini(
			@PageableDefault(
					size = 20,
					sort = "timestampInserimento",
					direction = Sort.Direction.DESC
			)
			Pageable pageable
	)
	{
		return ResponseEntity.ok(service.prendiOrdiniPaginati(pageable));
	}

	@GetMapping("/{id}/paginati")
	public ResponseEntity<PageResponseDto<OrdineResponseDto>> prendiOrdiniPerUtente(@PathVariable String  id,
																					@PageableDefault(
																							size = 20,
																							sort = "timestampInserimento",
																							direction = Sort.Direction.DESC
																					)
																					Pageable pageable)
	{
         return ResponseEntity.ok(service.prendiOrdiniPerUtentePaginati(id,pageable));
	}

}
