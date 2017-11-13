package com.gromoks.movieland.web.entity;

public enum RequestParameter {
    RATING("RATING"),
    PRICE("PRICE");

    private final String name;

    RequestParameter(String name) {
        this.name = name;
    }

<<<<<<< HEAD
    public static RequestParameter getByName(String name) {
        for(RequestParameter requestParameter : values()) {
            if (requestParameter.name.equalsIgnoreCase(name)) {
=======
    public static Enum getByRequestParamName(String name) {
        for(RequestParameter requestParameter : values()) {
            if (name.toUpperCase().equals(requestParameter.name)) {
>>>>>>> e80dcb2facb2aeb7dae916c7c7094cb4ceaafd8a
                return requestParameter;
            }
        }
        throw new IllegalArgumentException("Request parameter is not supported: " + name);
    }
}
