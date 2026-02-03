package com.java.test.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Result<T> {

	private boolean result;

	private String message;

	private T object;

	public Result(String message,T object,boolean result)
	{
		this.result = result;
		this.message = message;
		this.object = object;
	}

	public static <T> Result<T> successo(String message,T object)
	{
		return new Result<>(message,object,true);
	}

	public static <T> Result<T> fallimento(String message,T object)
	{
		return new Result<>(message,object,false);
	}



}
