package com.java.test.service.impl;

import com.java.test.annotation.CheckRoleAndOrder;
import com.java.test.annotation.ReadOnlyTransactional;
import com.java.test.dto.*;
import com.java.test.entity.*;
import com.java.test.enums.AnnulloEnum;
import com.java.test.enums.RuoloEnum;
import com.java.test.enums.StatoOrdineEnum;
import com.java.test.exception.*;
import com.java.test.mapper.OrdineMapper;
import com.java.test.mapper.ProdottoMapper;
import com.java.test.repository.*;
import com.java.test.service.IOrdineService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class OrdineService implements IOrdineService {

	private final OrdineRepository repository;

	private final UtenteRepository utenteRepository;

	private final StockRepository stockRepository;

	private final ProdottoMapper prodottoMapper;

	private final OrdineMapper ordineMapper;

	private final ProdottoRepository prodottoRepository;

	private final MovimentoRepository movimentoRepository;

	@Transactional
	@Override
	public OrdineEffettuatoResponseDto effettuaOrdine(String utenteId, Map<String,Integer> prodotti)
	{
		if(prodotti.isEmpty())
		{
			throw new ProdottoException("Nessun prodotto per l'ordine effettuato dall'utente","");
		}
		UtenteEntity utente = utenteRepository.findByEmail(utenteId)
				.orElseThrow(()->new UtenteException("Nessun utente trovato",utenteId));
		OrdineEntity ordine = new OrdineEntity(utente);
		OrdineEffettuatoResponseDto dto;
		try
		{
            for(var valore : prodotti.entrySet())
			{
				modificaProdottiEControllo(valore);
			}

			List<StockEntity> prodottiDaStock = stockRepository.findByProdotto_ProductIdIn(prodotti.keySet().stream().toList());
			if(prodottiDaStock.isEmpty())
			{
				throw new ProdottoException("Non riesco a trovare i prodotti per l'ordine nel sistema",
						Arrays.toString(prodotti.keySet().toArray()));
			}
			List<ProdottoConQuantitaResponseDto> listProdotti = new ArrayList<>();

			for(StockEntity entity : prodottiDaStock) {

				ordine.aggiungiProdotto(entity.getProdotto(),prodotti.get(entity.getProdotto().getProductId()));
			}
				ordine = repository.save(ordine);

				for(MovimentoEntity movimento : ordine.getMovimento())
				{
					listProdotti.add(creazioneProdotto(movimento));
				}
			dto = new OrdineEffettuatoResponseDto(new ProdottoConQuantitaListaDto(listProdotti),ordine.getOrdineId());


		}catch(DataIntegrityViolationException e)
		{
			throw new ApplicationException("Vincolo violato nella creazione l'ordine",e);
		}catch(DataAccessException e)
		{
			throw new ApplicationException("Errore generico nella creazione dell'ordine",e);
		}
        return dto;
	}

	@CheckRoleAndOrder
	@ReadOnlyTransactional
	@Override
	public OrdineResponseDto ricercaOrdinePerId(String id) {
		OrdineEntity ordine = repository.findByOrdineIdAndFlgAnnullo(id, AnnulloEnum.N.name()).orElseThrow(()->new OrdineException("Nessun ordine presente",id));
		return ordineMapper.toDto(id,ordine.getMovimento().stream().toList());
	}

	@ReadOnlyTransactional
	@Override
	public OrdiniResponseDto ricercaTuttiGliOrdini() {
		return ordineMapper.toListaDto(repository.findAll().stream().toList());
	}

	@CheckRoleAndOrder
	@Transactional
	@Override
	public OrdineResponseDto modificaProdotti(@P("id") String id, Map<String, Integer> prodotti) {
		for(var valore : prodotti.entrySet()) {
			controlloEsistenzaProdottoOrdine(id, valore.getKey());
		}
		OrdineEntity ordine = controlloEsistenzaOrdine(id,StatoOrdineEnum.CREATO);
		Set<MovimentoEntity> movimenti = ordine.getMovimento();
		for(MovimentoEntity movimento: movimenti)
		{
			Integer quantita = prodotti.get(movimento.getProdotto().getProductId());
			if(null != quantita)
			{
				if(quantita < 0)
				{
					throw new ProdottoException("Errore: il prodotto da modificare ha quantità negativa",movimento.getProdotto().getProductId());
				}else if(quantita == 0)
				{
					continue;
				}
				int nuovaQuantita = quantita + movimento.getQuantita();
				int modifica = stockRepository.modificaQuantitaProdotto(quantita,movimento.getProdotto().getProductId());
				if(modifica == 0)
				{
					throw new MagazzinoException("Errore: il prodotto non ha giacenze in magazzino",movimento.getProdotto()
							.getProductId(), nuovaQuantita);
				}
				movimento.modificaQuantita(nuovaQuantita);
			}

		}
		return ordineMapper.toDto(ordine.getOrdineId(),movimenti.stream().toList());
	}

	@CheckRoleAndOrder
	@Transactional
	@Override
	public OrdineEliminatiProdottiResponseDto eliminaProdotti(@P("id") String id, List<String> idPubbliciProdotti) {
		controlloEsistenzaOrdine(id,StatoOrdineEnum.CREATO);
		for(String prodotto:idPubbliciProdotti)
		{
			controlloEsistenzaProdottoOrdine(id,prodotto);
		}
		int modificati = repository.eliminaProdottiOrdine(id,idPubbliciProdotti);
		if(modificati != idPubbliciProdotti.size())
		{
			throw new ProdottoException("Prodotti invalidi durante l'eliminazione dall'ordine",idPubbliciProdotti.toString());
		}
		for(String prodotto : idPubbliciProdotti)
		{
			MovimentoEntity movimento = movimentoRepository.findByOrdine_OrdineIdAndProdotto_ProductId(id,prodotto)
					.orElseThrow(()->new MovimentoException("Non trovo il movimento da sistemare per eliminazione prodotti",id,prodotto));
			movimento.getProdotto().getStocK().aumentaQuantitaMagazzino(movimento.getQuantita());
		}
		return new OrdineEliminatiProdottiResponseDto(id,idPubbliciProdotti);
	}

	@CheckRoleAndOrder
	@Transactional
	@Override
	public OrdineResponseDto inserisciProdotti(@P("id") String id, Map<String, Integer> prodotti) {
		OrdineEntity ordine = controlloEsistenzaOrdine(id,StatoOrdineEnum.CREATO);
		for(var valore : prodotti.entrySet())
		{
			esistenzaProdottoOrdine(id,valore.getKey());
			modificaProdottiEControllo(valore);
			ProdottoEntity prodotto = prodottoRepository.findByProductId(valore.getKey())
					.orElseThrow(()->new ProdottoException("Il prodotto da aggiungere non esiste",valore.getKey()));
			ordine.aggiungiProdotto(prodotto,valore.getValue());
		}
		return ordineMapper.toDto(ordine.getOrdineId(), ordine.getMovimento().stream().toList());
	}

	@CheckRoleAndOrder
	@Transactional
	@Override
	public String cancellazioneOrdine(@P("id") String id) {
		controlloEsistenzaOrdine(id,StatoOrdineEnum.CREATO);
		List<MovimentoEntity> movimenti = movimentoRepository.findByOrdine_OrdineId(id);
         List<String> prodottiOrdine = movimenti.stream().map(mov->mov.getProdotto().getProductId()).toList();
		 for(MovimentoEntity movimento : movimenti)
		 {
			 int aggiunta = stockRepository.aggiungiQuantitaProdotto(movimento.getQuantita(),movimento.getProdotto().getProductId());
			 if(aggiunta == 0)
			 {
				 throw new MagazzinoException("Non sono riuscito a modificare il magazzino per la cancellazione dell'ordine",movimento.getProdotto().getProductId(),
						 movimento.getQuantita());
			 }
		 }
		int eliminaProdotti= repository.eliminaProdottiOrdine(id,movimenti.stream().map(mov->mov.getProdotto().getProductId()).toList());

		if(eliminaProdotti != prodottiOrdine.size())
		{
			throw new OrdineException("Non sono riuscito a cancellare l'ordine", id);
		}

		 int ordine = repository.eliminaOrdine(id);
		 if(ordine == 0)
		 {
			 throw new OrdineException("Non sono riuscito a cancellare l'ordine", id);
		 }
		 return id;
	}

	@ReadOnlyTransactional
	@Override
	public PageResponseDto<OrdineResponseDto> prendiOrdiniPaginati(Pageable pageable) {
		Page<OrdineEntity> ordini = repository.findAll(pageable);
		return componiOrdinePaginato(ordini);
	}

	@ReadOnlyTransactional
	@Override
	public PageResponseDto<OrdineResponseDto> prendiOrdiniPerUtentePaginati(String id, Pageable pageable) {
		Page<OrdineEntity> ordini = repository.findByUtente_Email(id,pageable);
		return componiOrdinePaginato(ordini);
	}

	@ReadOnlyTransactional
	@Override
	public PageResponseDto<OrdineResponseDto> prendiOrdiniPerUtentePaginatiUtente(String id, Pageable pageable) {
		Page<OrdineEntity> ordini = repository.findByUtente_UtenteId(id,pageable);
		return componiOrdinePaginato(ordini);
	}

	@CheckRoleAndOrder
	@Transactional
	@Override
	public OrdineResponseDto diminuisciQuantitaProdotto(@P("id") String idOrdine, String idProdotto, int quantita) {

		if (quantita <= 0) {
			throw new ProdottoException("La quantità da diminuire deve essere maggiore di 0", idProdotto);
		}

		OrdineEntity ordine = controlloEsistenzaOrdine(idOrdine, StatoOrdineEnum.CREATO);

		controlloEsistenzaProdottoOrdine(idOrdine, idProdotto);

		MovimentoEntity movimento = movimentoRepository
				.findByOrdine_OrdineIdAndProdotto_ProductId(idOrdine, idProdotto)
				.orElseThrow(() -> new MovimentoException("Non trovo il movimento da diminuire", idOrdine, idProdotto));

		int attuale = movimento.getQuantita();
		if (quantita > attuale) {
			throw new ProdottoException("Non puoi diminuire più della quantità presente nell'ordine", idProdotto);
		}

		int aggiunta = stockRepository.aggiungiQuantitaProdotto(quantita, idProdotto);
		if (aggiunta == 0) {
			throw new MagazzinoException("Non sono riuscito a ripristinare il magazzino durante la diminuzione prodotto",
					idProdotto, quantita);
		}

		int nuovaQuantita = attuale - quantita;

		if (nuovaQuantita == 0) {
			movimentoRepository.delete(movimento);
			ordine.getMovimento().remove(movimento);
		} else {
			movimento.modificaQuantita(nuovaQuantita);
		}

		return ordineMapper.toDto(ordine.getOrdineId(), ordine.getMovimento().stream().toList());
	}



	private ProdottoConQuantitaResponseDto creazioneProdotto(MovimentoEntity movimento)
	{
		return prodottoMapper.toDtoStock(movimento.getProdotto(),movimento.getProdotto()
				.getStocK());
	}

	private void modificaProdottiEControllo(Map.Entry<String, Integer> valore)
	{
		int elementiModificati = stockRepository.modificaQuantitaProdotto(valore.getValue(),valore.getKey());
		if(elementiModificati == 0)
		{
			throw new MagazzinoException("Errore: il prodotto non ha giacenze in magazzino",valore.getKey(),valore.getValue());
		}
	}

	private OrdineEntity controlloEsistenzaOrdine(String id,StatoOrdineEnum stato)
	{
		return repository.findByOrdineIdAndStatoOrdine(id, stato).orElseThrow(()->new OrdineException(
				"L'ordine non è presente o ha uno stato invalido",id
		));
	}

	private void esistenzaProdottoOrdine(String idOrdine,String idProdotto)
	{
		if(movimentoRepository.existsByOrdine_OrdineIdAndProdotto_ProductId(idOrdine,idProdotto))
		{
			throw new MovimentoException("Il prodotto è già presente nell'ordine, non puoi aggiungerlo",idOrdine,idProdotto);
		}
	}

	private void controlloEsistenzaProdottoOrdine(String idOrdine,String idProdotto)
	{
		if(!movimentoRepository.existsByOrdine_OrdineIdAndProdotto_ProductId(idOrdine,idProdotto))
		{
			throw new MovimentoException("Il prodotto non è presente nell'ordine, non puoi aggiungerlo",idOrdine,idProdotto);
		}
	}

	private PageResponseDto<OrdineResponseDto> componiOrdinePaginato(Page<OrdineEntity> ordini)
	{
		OrdiniResponseDto ordiniResponse = ordineMapper.toListaDto(ordini.getContent());
		return new PageResponseDto<>(ordiniResponse.ordini(),ordini.getNumber(),
				ordini.getSize(),ordini.getTotalElements(),ordini.getTotalPages());
	}

}
