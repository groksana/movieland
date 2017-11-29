package com.gromoks.movieland.service.entity;

public enum UserRole {
    USER("USER"), ADMIN("ADMIN"), GUEST("GUEST");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole getByName(String name) {
        if (name != null) {
            for (UserRole userRole : values()) {
                if (userRole.name.equalsIgnoreCase(name)) {
                    return userRole;
                }
            }
        } else {return GUEST;}
        throw new IllegalArgumentException("Conversion for UserRole is not supported. UserRole = " + name);
    }
}
