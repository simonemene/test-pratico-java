package com.java.test.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "ORDINE")
public class OrdineEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany(mappedBy = "ordine", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	private Set<StockEntity> stock;

	@ManyToOne
	@JoinColumn(name = "ID_UTENTE",nullable = false)
	private UtenteEntity utente;


}
