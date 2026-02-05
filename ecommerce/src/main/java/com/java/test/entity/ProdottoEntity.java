package com.java.test.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "PRODOTTO")
public class ProdottoEntity {

	protected ProdottoEntity()
	{

	}

	public ProdottoEntity(BigDecimal prezzo,String nome)
	{
		this.prezzo = prezzo;
	    this.nome = nome;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Setter(AccessLevel.NONE)
	@Column(name = "ID_PUBBLICO_PRODOTTO",nullable = false,unique = true,length = 36,updatable = false)
	private String productId;

	@OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
	private Set<MovimentoEntity> movimento = new HashSet<>();

	@OneToOne(mappedBy = "prodotto")
	private StockEntity stocK;

	private BigDecimal prezzo;

	@Column(unique = true)
	private String nome;

	@PrePersist
	private void persist()
	{
		this.productId = UUID.randomUUID().toString();
	}
}
