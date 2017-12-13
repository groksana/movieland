package com.gromoks.movieland.service.impl.cache;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.cache.ReportCache;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReportCacheImpl implements ReportCache {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, ReportRequest> CACHED_REPORT_REQUEST_MAP = new ConcurrentHashMap<>();

    @Override
    public void addRequest(ReportRequest reportRequest) {
        log.debug("Start to add report request to cache with id = {}", reportRequest.getRequestUuid());

        CACHED_REPORT_REQUEST_MAP.put(reportRequest.getRequestUuid(), reportRequest);

        log.debug("Finish to add report request to cache");
    }

    @Override
    public List<ReportRequest> getNewRequests() {
        log.debug("Start to get new request for processing from cached map");

        List<ReportRequest> reportRequests = new ArrayList<>();
        for (Map.Entry<String, ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getValue().getStatus() == ReportStatus.NEW) {
                reportRequests.add(entry.getValue());
            }
        }

        log.debug("Finish to get new request for processing from cached map. Request count = {}", reportRequests.size());
        return reportRequests;
    }

    @Override
    public boolean changeReportRequestStatus(String reportRequestUuid, ReportStatus currentReportStatus, ReportStatus newReportStatus) {
        log.debug("Start to change report request status for request = {}", reportRequestUuid);

        boolean changeResult = false;
        for (Map.Entry<String, ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getKey().equals(reportRequestUuid)) {
                if (entry.getValue().getStatus() == currentReportStatus) {
                    entry.getValue().setStatus(newReportStatus);
                    changeResult = true;
                    log.debug("Status has been changed");
                } else {
                    log.debug("Current status in map doesn't match with provided status {}", currentReportStatus);
                }
            }
        }

        log.debug("Finish to change report request status for request = {}", reportRequestUuid);
        return changeResult;
    }

    @Override
    public List<ReportRequest> getReportRequestStatusByUser(User user) {
        log.info("Start to get report request status by user {}", user.getNickname());

        List<ReportRequest> reportRequests = new ArrayList<>();

        for (Map.Entry<String,ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getValue().getRequestedUser().getEmail().equals(user.getEmail())) {
                reportRequests.add(entry.getValue());
            }
        }

        log.info("Finish to get report request status by user {}", user.getNickname());
        return reportRequests;
    }


}
