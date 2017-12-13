package com.gromoks.movieland.service.entity;

public enum ReportOutputType {
    PDF("PDF"), XLSX("XLSX");

    private String name;

    ReportOutputType(String name) {
        this.name = name;
    }

    public static ReportOutputType getByName(String name) {
        for (ReportOutputType reportOutputType : values()) {
            if (reportOutputType.name.equalsIgnoreCase(name)) {
                return reportOutputType;
            }
        }
        throw new IllegalArgumentException("Report output type is not supported: " + name);
    }
}
