package com.java.test.exception;

public class OrdineException extends RuntimeException {

	private String ordineId;

	public OrdineException(String message,String ordineId) {

		super(message);
		this.ordineId = ordineId;
	}
}
