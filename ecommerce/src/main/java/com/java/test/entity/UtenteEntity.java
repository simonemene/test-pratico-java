package com.java.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "UTENTE")
public class UtenteEntity  extends AuditEntity{

	protected UtenteEntity()
	{

	}

	public UtenteEntity(String email,String nome,String cognome,String codiceFiscale,String password)
	{
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.password = password;
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

	@Getter(AccessLevel.NONE)
	@Column(nullable = false)
	private String password;

	@Column(unique = true,nullable = false)
	private String codiceFiscale;

	@ManyToOne(optional = false)
	@JoinColumn(name="ID_RUOLO",nullable = false)
	private RuoloEntity ruolo;

	@Version
	private int version;

	@OneToMany(mappedBy = "utente",fetch = FetchType.LAZY)
	private Set<OrdineEntity> ordine = new HashSet<>();

	@PrePersist
	public void persist()
	{
		this.utenteId = UUID.randomUUID().toString();
	}

	public String passwordHash()
	{
		return this.password;
	}
}
