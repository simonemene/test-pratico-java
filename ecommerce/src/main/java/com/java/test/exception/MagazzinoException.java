package com.java.test.exception;

import lombok.Getter;

@Getter
public class MagazzinoException extends RuntimeException {

	private final String productId;

	private final int quantita;

	public MagazzinoException(String message,String productId,int quantita) {
		super(message);
		this.productId = productId;
		this.quantita = quantita;
	}
}
