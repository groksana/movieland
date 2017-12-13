package com.gromoks.movieland.service.entity;

public enum ReportType {
    ALL_MOVIES("ALL_MOVIES"), ADDED_DURING_PERIOD("ADDED_DURING_PERIOD"), TOP_ACTIVE_USERS("TOP_ACTIVE_USERS");

    private String name;

    ReportType(String name) {
        this.name = name;
    }

    public static ReportType getByName(String name) {
        for (ReportType reportType : values()) {
            if (reportType.name.equalsIgnoreCase(name)) {
                return reportType;
            }
        }
        throw new IllegalArgumentException("Report type is not supported: " + name);
    }
}
