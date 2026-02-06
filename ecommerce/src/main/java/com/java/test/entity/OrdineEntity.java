package com.java.test.entity;

import com.java.test.enums.StatoOrdineEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "ORDINE")
public class OrdineEntity  extends AuditEntity{

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

	@Version
	private int version;

	@Enumerated(EnumType.STRING)
	@Column(name =  "STATO")
	private StatoOrdineEnum statoOrdine;

	@PrePersist
	public void init()
	{
		this.ordineId = UUID.randomUUID().toString();
		this.statoOrdine = StatoOrdineEnum.CREATO;
	}

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

	public void sostituisciProdotti(Set<MovimentoEntity> listaMovimenti)
	{
		this.movimento = listaMovimenti;
	}
}
