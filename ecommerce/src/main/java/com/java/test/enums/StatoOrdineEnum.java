package com.java.test.enums;

public enum StatoOrdineEnum {

	CREATO("CREATO"),
	CONSEGNATO("CONSEGNATO"),
	ANNULLATO("ANNULLATO");

	private String stato;

	private StatoOrdineEnum(String stato)
	{
		this.stato = stato;
	}
}
