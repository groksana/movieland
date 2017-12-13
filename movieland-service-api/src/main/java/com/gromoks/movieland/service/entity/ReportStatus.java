package com.gromoks.movieland.service.entity;

public enum ReportStatus {
    NEW("NEW"), IN_PROGRESS("IN_PROGRESS"), COMPLETED("COMPLETED"), REJECTED("REJECTED");

    private final String name;

    ReportStatus(String name) {
        this.name = name;
    }

    public static ReportStatus getByName(String name) {
        for (ReportStatus reportStatus : values()) {
            if (reportStatus.name.equalsIgnoreCase(name)) {
                return reportStatus;
            }
        }
        throw new IllegalArgumentException("Report status is not supported: " + name);
    }
}
