package com.gromoks.movieland.service.entity;

public enum UserRole {
    USER("USER"), ADMIN("ADMIN");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole getByName(String name) {
        for (UserRole userRole : values()) {
            if (userRole.name.equalsIgnoreCase(name)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Conversion for UserRole is not supported. UserRole = " + name);
    }
}
