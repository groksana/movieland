package com.gromoks.movieland.web.entity;

public enum OrderType {
    ASC("ASC"),
    DESC("DESC");

    private final String name;

    OrderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
