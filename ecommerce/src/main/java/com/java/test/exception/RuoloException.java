package com.java.test.exception;

public class RuoloException extends RuntimeException {

  private String ruolo;

    public RuoloException(String message,String ruolo) {
        super(message);
        this.ruolo = ruolo;
    }
}
