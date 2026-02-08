package com.java.test.enums;

public enum RuoloEnum {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String ruolo;

    private RuoloEnum(String ruolo)
    {
        this.ruolo = ruolo;
    }
}

