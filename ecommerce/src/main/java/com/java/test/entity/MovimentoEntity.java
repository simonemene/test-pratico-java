package com.java.test.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "MOVIMENTO",
uniqueConstraints = @UniqueConstraint(
		name = "movimento_ordine_prodotto",
		columnNames = {"ID_ORDINE","ID_PRODOTTO"}
))
public class MovimentoEntity extends AuditEntity{

	protected MovimentoEntity()
	{

	}

	public MovimentoEntity(ProdottoEntity prodotto,int quantita,OrdineEntity ordine)
	{
		this.prodotto = prodotto;
		this.quantita = quantita;
		this.ordine = ordine;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORDINE", nullable = false)
	private OrdineEntity ordine;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PRODOTTO", nullable = false)
	private ProdottoEntity prodotto;

	@Column(nullable = false)
	private int quantita;

	@Version
	private int version;

	public void modificaQuantita(int quantita)
	{
		this.quantita = quantita;
	}

}
