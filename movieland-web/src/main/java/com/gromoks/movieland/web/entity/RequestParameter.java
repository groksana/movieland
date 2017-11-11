package com.gromoks.movieland.web.entity;

public enum RequestParameter {
    RATING("RATING"),
    PRICE("PRICE");

    private final String name;

    RequestParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
