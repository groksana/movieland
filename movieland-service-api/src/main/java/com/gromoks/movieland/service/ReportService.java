package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;

import java.util.List;

public interface ReportService {
    void addReportRequest(ReportRequest reportRequest);

    void processReportRequest();

    byte[] getReport(String filename);

    List<ReportRequest> getReportRequestStatusByUser(User user);

    List<ReportInfo> getReportLinkByEmail(String email);
}
