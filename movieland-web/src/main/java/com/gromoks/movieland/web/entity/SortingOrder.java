package com.gromoks.movieland.web.entity;

public enum SortingOrder {
    ASC("ASC"), DESC("DESC");

    private final String name;

    SortingOrder(String name) {
        this.name = name;
    }

    public static SortingOrder getByName(String name){
        for (SortingOrder sortingOrder : values()) {
            if (sortingOrder.name.equalsIgnoreCase(name)) {
                return sortingOrder;
            }
        }

        throw new IllegalArgumentException("Sorting type is not supported: " + name);
    }
}

