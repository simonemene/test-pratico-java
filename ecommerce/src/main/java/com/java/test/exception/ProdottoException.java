package com.java.test.exception;

import lombok.Getter;

@Getter
public class ProdottoException extends RuntimeException {

	private final String productId;

	public ProdottoException(String message,String productId) {
		super(message);
		this.productId = productId;
	}
}
