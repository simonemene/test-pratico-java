package com.java.test.controller;

import com.java.test.annotation.CurrentUser;
import com.java.test.dto.*;
import com.java.test.enums.StatoOrdineEnum;
import com.java.test.service.IOrdineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
		name = "Controller Ordini",
		description = "API per la gestione degli ordini dei clienti"
)
@RequestMapping("/api/ordine")
@RequiredArgsConstructor
@RestController
public class OrdineController {

	private final IOrdineService service;

    @Operation(
			summary="Creazione ordini",
			description = "API per la creazione di un nuovo ordine"
	)
	@PostMapping
	public ResponseEntity<OrdineEffettuatoResponseDto> effettuaOrdine(@RequestBody @Valid
			OrdineRequestDto ordineRequestDto,@CurrentUser String userId)
	{
		OrdineEffettuatoResponseDto response = service.effettuaOrdine(userId,ordineRequestDto.prodotti());
		URI path = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.idPubblico())
				.toUri();
		return ResponseEntity.created(path).body(response);
	}

	@Operation(
			summary="Estrarre tutti gli ordini",
			description = "API per l'estrazione degli ordini"
	)
	@GetMapping
	public ResponseEntity<OrdiniResponseDto> prendiTuttiGliOrdini()
	{
		return ResponseEntity.ok(service.ricercaTuttiGliOrdini());
	}

	@Operation(
			summary="Estrazione di uno specifico ordine",
			description = "API per l'estrazione di un ordine specifico di un cliente"
	)
	@GetMapping("/{id}")
	public ResponseEntity<OrdineResponseDto> prendiOrdine(@PathVariable @NotBlank(message = "L'id dell'ordine non può essere vuoto") String id)
	{
		return ResponseEntity.ok(service.ricercaOrdinePerId(id));
	}

	@Operation(
			summary="Modifica di un ordine",
			description = "API per la modifica di un prodotto all'interno di un ordine"
	)
	@PatchMapping("/{id}/prodotti")
	public ResponseEntity<OrdineResponseDto> modificaProdotti(@PathVariable String id,@RequestBody OrdineRequestDto prodottiModificati)
	{
		return ResponseEntity.ok(service.modificaProdotti(id,prodottiModificati.prodotti()));
	}

	@Operation(
			summary="Eliminazione di un prodotto",
			description = "API per l'eliminazione di un prodotto all'interno di un ordine"
	)
	@DeleteMapping("/{id}/prodotti")
	public ResponseEntity<Void> eliminaProdotti(@PathVariable String id,@RequestBody OrdiniCancellatiRequestDto prodottiEliminati)
	{
		service.eliminaProdotti(id,prodottiEliminati.prodotti());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
			summary="Inserire nuovo prodotto",
			description = "API per l'inserimento di un nuovo prodotto all'interno di un ordine"
	)
	@PostMapping("/{id}/prodotti")
	public ResponseEntity<OrdineResponseDto> inserisciProdotti(@PathVariable String id,@RequestBody OrdineRequestDto inserimentoProdotti)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(service.inserisciProdotti(id,inserimentoProdotti.prodotti()));
	}

	@Operation(
			summary="Eliminazione di un ordine",
			description = "API per l'eliminazione completa di un ordine e di tutti i suoi prodotti"
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaOrdine(@PathVariable String id)
	{
		service.cancellazioneOrdine(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
			summary="Estrazione paginata dei prodotti",
			description = "API per l'estrazione paginata di tutti i prodotti ordinati"
	)
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

	@Operation(
			summary="Estrazione paginata prodotti per utente",
			description = "API per l'estrazione paginata degli ordini di un utente"
	)
	@GetMapping("/utente/paginati")
	public ResponseEntity<PageResponseDto<OrdineResponseDto>> prendiOrdiniPerUtente(@CurrentUser String  id,
																					@PageableDefault(
																							size = 20,
																							sort = "timestampInserimento",
																							direction = Sort.Direction.DESC
																					)
																					Pageable pageable)
	{
         return ResponseEntity.ok(service.prendiOrdiniPerUtentePaginati(id,pageable));
	}

	@Operation(
			summary="Estrazione paginata ordini",
			description = "API per l'estrazione paginata di ordini appartenenti ad un utente"
	)
	@GetMapping("/{id}/paginati")
	public ResponseEntity<PageResponseDto<OrdineResponseDto>> prendiOrdiniPerUtenteConId(
			@NotBlank(message = "L'id dell'ordine non può essere vuoto")
			@PathVariable String  id,
			@PageableDefault(
					size = 20,
					sort = "timestampInserimento",
					direction = Sort.Direction.DESC
			)
			Pageable pageable)
	{
		return ResponseEntity.ok(service.prendiOrdiniPerUtentePaginatiUtente(id,pageable));
	}

	@Operation(
			summary="Diminuzione numero prodotti",
			description = "API per la diminuzione di prodotti all'interno di un ordine"
	)
	@PatchMapping("/{id}/prodotti/diminuisci")
	public ResponseEntity<OrdineResponseDto> diminuisciQuantitaProdotto(
			@PathVariable String id,
			@RequestBody @Valid DiminuisciProdottoOrdineRequestDto request
	) {
		return ResponseEntity.ok(
				service.diminuisciQuantitaProdotto(id, request.prodottoId(), request.quantita())
		);
	}

	@Operation(
			summary="Modifica stato ordine",
			description = "API per la modifica dello stato di un ordine"
	)
	@PatchMapping("/{id}/stato/{stato}")
	public ResponseEntity<OrdineResponseDto> modificaStatoOrdine(
			@NotBlank(message = "L'id dell'ordine non può essere vuoto")  @PathVariable String id,
			@PathVariable StatoOrdineEnum stato
	) {
		return ResponseEntity.ok(
				service.modificaStatoOrdine(id,stato));

	}


}
