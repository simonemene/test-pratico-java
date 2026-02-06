package com.java.test.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ProdottoException extends RuntimeException {

	private final String[] productId;

	public ProdottoException(String message,String... productId) {
		super(message);
		this.productId = productId;
	}
}
