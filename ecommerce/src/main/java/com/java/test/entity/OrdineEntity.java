package com.java.test.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "ORDINE")
public class OrdineEntity {

	protected OrdineEntity()
	{

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany(mappedBy = "ordine", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	private Set<StockEntity> stock = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "ID_UTENTE",nullable = false)
	private UtenteEntity utente;

	public void collegaUtente(UtenteEntity utente)
	{
		this.utente = utente;
	}

	public StockEntity aggiungiProdotto(ProdottoEntity prodotto, int quantita)
	{
		StockEntity stock =  new StockEntity(prodotto,quantita,this);
		this.stock.add(stock);
		return stock;
	}
}
