package com.java.test.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "STOCK",
uniqueConstraints = @UniqueConstraint(
		name = "sotck_ordine_prodotto",
		columnNames = {"ID_ORDINE","ID_PRODOTTO"}
))
public class StockEntity {

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

}
