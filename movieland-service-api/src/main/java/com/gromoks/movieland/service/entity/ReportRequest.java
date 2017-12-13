package com.gromoks.movieland.service.entity;

import com.gromoks.movieland.entity.User;

public class ReportRequest {
    private String requestUuid;
    private ReportType reportType;
    private ReportOutputType reportOutputType;
    private User requestedUser;
    private ReportStatus status;
    private String requestUrl;

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportOutputType getReportOutputType() {
        return reportOutputType;
    }

    public void setReportOutputType(ReportOutputType reportOutputType) {
        this.reportOutputType = reportOutputType;
    }

    public User getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(User requestedUser) {
        this.requestedUser = requestedUser;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
