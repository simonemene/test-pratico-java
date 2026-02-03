package com.java.test.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Setter
@Entity
@Table(name = "PRODOTTO")
public class ProdottoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Setter(AccessLevel.NONE)
	@Column(name = "ID_PUBBLICO_PRODOTTO",nullable = false,unique = true,length = 36,updatable = false)
	private String productId;

	@OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
	private Set<StockEntity> stock;

	private BigDecimal prezzo;

	@PrePersist
	private void persist()
	{
		this.productId = UUID.randomUUID().toString();
	}
}
