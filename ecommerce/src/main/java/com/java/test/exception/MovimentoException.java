package com.java.test.exception;

public class MovimentoException extends RuntimeException {

	private String idOrdine;
	private String idProdotto;

	public MovimentoException(String message,String idOrdine,String idProdotto) {
		super(message);
		this.idOrdine = idOrdine;
		this.idProdotto =idProdotto;
	}
}
