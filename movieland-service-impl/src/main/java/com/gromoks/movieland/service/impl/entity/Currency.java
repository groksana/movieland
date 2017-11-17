package com.gromoks.movieland.service.impl.entity;

public enum Currency {
    USD("USD"), EUR("EUR");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public static Currency getByName(String name) {
        for (Currency currency : values()) {
            if (currency.name.equalsIgnoreCase(name)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Conversion for Currency is not supported. Currency = " + name);
    }
}