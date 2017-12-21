package com.gromoks.movieland.service.cache;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;

import java.util.List;

public interface ReportCache {
    void addRequest(ReportRequest reportRequest);

    void changeReportRequestStatus(String reportRequestUuid, ReportStatus newReportStatus);

    List<ReportRequest> getReportRequestStatusByUser(User user);

    void removeReportRequest(ReportRequest reportRequest);

    ReportRequest poll();
}
