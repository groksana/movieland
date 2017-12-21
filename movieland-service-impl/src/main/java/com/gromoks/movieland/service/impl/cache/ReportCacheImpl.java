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
    public void changeReportRequestStatus(String reportRequestUuid, ReportStatus newReportStatus) {
        log.debug("Start to change report request status for request = {}", reportRequestUuid);

        for (Map.Entry<String, ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getKey().equals(reportRequestUuid)) {
                entry.getValue().setStatus(newReportStatus);
                log.debug("Status has been changed");
                break;
            }
        }

        log.debug("Finish to change report request status for request = {}", reportRequestUuid);
    }

    @Override
    public List<ReportRequest> getReportRequestStatusByUser(User user) {
        log.info("Start to get report request status by user {}", user.getNickname());

        List<ReportRequest> reportRequests = new ArrayList<>();

        for (Map.Entry<String, ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getValue().getRequestedUser().getEmail().equals(user.getEmail())) {
                reportRequests.add(entry.getValue());
            }
        }

        log.info("Finish to get report request status by user {}", user.getNickname());
        return reportRequests;
    }

    @Override
    public void removeReportRequest(ReportRequest reportRequest) {
        log.info("Start to remove report request from cache with name {}", reportRequest.getRequestUuid());

        CACHED_REPORT_REQUEST_MAP.remove(reportRequest.getRequestUuid());

        log.info("Finish to remove report request from cache with name {}", reportRequest.getRequestUuid());
    }

    @Override
    public ReportRequest poll() {
        for (Map.Entry<String, ReportRequest> entry : CACHED_REPORT_REQUEST_MAP.entrySet()) {
            if (entry.getValue().getStatus() == ReportStatus.NEW) {
                entry.getValue().setStatus(ReportStatus.IN_PROGRESS);
                return entry.getValue();
            }
        }
        return null;
    }


}
