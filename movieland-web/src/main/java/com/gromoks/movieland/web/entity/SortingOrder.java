package com.gromoks.movieland.web.entity;

public enum SortingOrder {
    ASC("ASC"), DESC("DESC");

    public final String name;

    SortingOrder(String name) {
        this.name = name;
    }

<<<<<<< HEAD
    public static SortingOrder getByName(String name){
        for (SortingOrder sortingOrder : values()) {
            if (sortingOrder.name.equalsIgnoreCase(name)) {
=======
    public static Enum getBySortingOrderName(String name){
        for (SortingOrder sortingOrder : values()) {
            if (name.toUpperCase().equals(sortingOrder.name)) {
>>>>>>> e80dcb2facb2aeb7dae916c7c7094cb4ceaafd8a
                return sortingOrder;
            }
        }

        throw new IllegalArgumentException("Sorting type is not supported: " + name);
    }
}
<<<<<<< HEAD

=======
>>>>>>> e80dcb2facb2aeb7dae916c7c7094cb4ceaafd8a
