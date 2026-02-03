package com.java.test.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "UTENTE")
public class UtenteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String email;

	private String nome;

	private String cognome;

	private String codiceFiscale;

	@OneToMany(mappedBy = "utente",fetch = FetchType.LAZY)
	private Set<OrdineEntity> ordine;
}
