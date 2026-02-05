package com.java.test.exception;

import lombok.Getter;

@Getter
public class UtenteException extends RuntimeException
{
	private final String utenteId;

	public UtenteException(String message,String utenteId) {
		super(message);
		this.utenteId = utenteId;
	}
}
