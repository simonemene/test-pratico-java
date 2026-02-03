package com.java.test.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "PRODOTTO")
public class ProdottoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
	private Set<StockEntity> stock;

	private BigDecimal prezzo;
}
