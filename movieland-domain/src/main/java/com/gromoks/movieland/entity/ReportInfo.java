package com.gromoks.movieland.entity;

public class ReportInfo {
    private ReportType reportType;
    private String recipient;
    private String reportLink;

    public ReportInfo() {
    }

    public ReportInfo(ReportType reportType, String recipient, String reportLink) {
        this.reportType = reportType;
        this.recipient = recipient;
        this.reportLink = reportLink;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
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

    @Override
    public String toString() {
        return "ReportInfo{" +
                "reportType=" + reportType +
                ", recipient='" + recipient + '\'' +
                ", reportLink='" + reportLink + '\'' +
                '}';
    }
}
