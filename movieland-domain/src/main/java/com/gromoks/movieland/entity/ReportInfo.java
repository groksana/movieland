package com.gromoks.movieland.entity;

public class ReportInfo {
    private String reportType;
    private String recipient;
    private String reportLink;

    public ReportInfo() {
    }

    public ReportInfo(String reportType, String recipient, String reportLink) {
        this.reportType = reportType;
        this.recipient = recipient;
        this.reportLink = reportLink;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }
}
