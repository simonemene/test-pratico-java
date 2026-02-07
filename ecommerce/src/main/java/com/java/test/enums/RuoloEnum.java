package com.java.test.enums;

public enum RuoloEnum {

    USER("USER"),
    ADMIN("ADMIN");

    private String ruolo;

    private RuoloEnum(String ruolo)
    {
        this.ruolo = ruolo;
    }
}

