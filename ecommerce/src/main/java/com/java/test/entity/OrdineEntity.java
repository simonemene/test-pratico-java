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

	public OrdineEntity(UtenteEntity utente)
	{
		this.utente = utente;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "ORDINE_ID",unique = true,nullable = false)
	private String ordineId;

	@OneToMany(mappedBy = "ordine", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	private Set<MovimentoEntity> movimento = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "ID_UTENTE",nullable = false)
	private UtenteEntity utente;

	public void collegaUtente(UtenteEntity utente)
	{
		this.utente = utente;
	}

	public MovimentoEntity aggiungiProdotto(ProdottoEntity prodotto, int quantita)
	{
		MovimentoEntity movimento =  new MovimentoEntity(prodotto,quantita,this);
		this.movimento.add(movimento);
		return movimento;
	}
}
