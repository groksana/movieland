package com.gromoks.movieland.service.cache;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;

import java.util.List;

public interface ReportCache {
    void addRequest(ReportRequest reportRequest);

    List<ReportRequest> getNewRequests();

    boolean changeReportRequestStatus(String reportRequestUuid, ReportStatus currentReportStatus, ReportStatus newReportStatus);

    List<ReportRequest> getReportRequestStatusByUser(User user);
}
