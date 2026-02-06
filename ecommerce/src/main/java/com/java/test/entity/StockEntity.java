package com.java.test.entity;

import com.java.test.exception.MagazzinoException;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "STOCK")
@Entity
public class StockEntity  extends AuditEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int quantita;

	@OneToOne(optional = false,cascade = CascadeType.PERSIST)
	@JoinColumn(name = "PRODOTTO_ID",unique = true,nullable = false)
	private ProdottoEntity prodotto;

	public StockEntity(int quantita)
	{
		this.quantita = quantita;
	}

	protected StockEntity()
	{

	}

	public void collegaProdotto(ProdottoEntity prodotto)
	{
		this.prodotto = prodotto;
	}


	public int modificaQuantitaMagazzino(int diminuzione)
	{
		int nuovaQuantita = quantita - diminuzione;
		if(nuovaQuantita <= 0)
		{
			throw new MagazzinoException("Non è stato possibile diminuire la quantità del magazzino",this.prodotto.getProductId(),this.quantita);
		}
		this.quantita = nuovaQuantita;
		return this.quantita;
	}

	public int aumentaQuantitaMagazzino(int aumento)
	{
		this.quantita = quantita + aumento;
		return this.quantita;
	}
}
