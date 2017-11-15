package com.gromoks.movieland.web.entity;

public enum RequestParameter {
    RATING("RATING"),
    PRICE("PRICE");

    private final String name;

    RequestParameter(String name) {
        this.name = name;
    }

    public static RequestParameter getByName(String name) {
        for(RequestParameter requestParameter : values()) {
            if (requestParameter.name.equalsIgnoreCase(name)) {
                return requestParameter;
            }
        }
        throw new IllegalArgumentException("Request parameter is not supported: " + name);
    }
}

