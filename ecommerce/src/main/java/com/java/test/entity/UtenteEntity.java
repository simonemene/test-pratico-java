package com.java.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "UTENTE")
public class UtenteEntity {

	protected UtenteEntity()
	{

	}

	public UtenteEntity(String email,String nome,String cognome,String codiceFiscale)
	{
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true,nullable = false)
	private String email;

	@Column(name = "UTENTE_ID",unique = true,nullable = false)
	private String utenteId;

	private String nome;

	private String cognome;

	@Column(unique = true,nullable = false)
	private String codiceFiscale;

	@OneToMany(mappedBy = "utente",fetch = FetchType.LAZY)
	private Set<OrdineEntity> ordine = new HashSet<>();

	@PrePersist
	public void persist()
	{
		this.utenteId = UUID.randomUUID().toString();
	}

	public void aggiungiOrdine(OrdineEntity ordineDaAggiungere)
	{
		ordineDaAggiungere.collegaUtente(this);
		this.ordine.add(ordineDaAggiungere);
	}
}
