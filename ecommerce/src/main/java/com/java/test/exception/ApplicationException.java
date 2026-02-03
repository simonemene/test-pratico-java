package com.java.test.exception;

public class ApplicationException extends RuntimeException {
  public ApplicationException(String message,Throwable causa) {
    super(message,causa);
  }
}
