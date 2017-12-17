package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.IoReportDao;
import com.gromoks.movieland.dao.ReportDao;
import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.EmailService;
import com.gromoks.movieland.service.GenerateReportService;
import com.gromoks.movieland.service.ReportService;
import com.gromoks.movieland.service.cache.ReportCache;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

import static com.gromoks.movieland.service.impl.util.ReportUtil.*;
import static com.gromoks.movieland.service.impl.util.ReportUtil.getReportNameFromRequest;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private IoReportDao ioReportDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GenerateReportService generateReportService;

    @Override
    public void addReportRequest(ReportRequest reportRequest) {
        reportCache.addRequest(reportRequest);
    }

    @Override
    @Scheduled(fixedRateString = "${report.fixedRate}", initialDelayString = "${report.fixedRate}")
    @Transactional
    public void processReportRequests() {
        log.info("Start to process report requests");
        List<ReportRequest> reportRequests = reportCache.getRequestsForProcessing();

        for (ReportRequest reportRequest : reportRequests) {
            generateReportService.generateReport(reportRequest);

            String text = createReportEmailMessage(reportRequest);
            emailService.sendMail(reportRequest.getRequestedUser().getEmail(), text);

            String reportLink = reportRequest.getRequestUrl() + "/" + getReportNameFromRequest(reportRequest);
            String email = reportRequest.getRequestedUser().getEmail();
            reportDao.insertReportInfo(new ReportInfo(reportRequest.getReportType(), email, reportLink));

            reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.IN_PROGRESS, ReportStatus.COMPLETED);
        }

        log.info("Finish to process report requests");
    }

    @Override
    public InputStream getReport(String filename) {
        return ioReportDao.getReport(filename);
    }

    @Override
    public List<ReportRequest> getReportRequestStatusByUser(User user) {
        return reportCache.getReportRequestStatusByUser(user);
    }

    @Override
    public List<ReportInfo> getReportLinkByEmail(String email) {
        return reportDao.getReportLinkByEmail(email);
    }

    @Override
    public void removeReport(ReportRequest reportRequest) {
        log.debug("Start to remove report and report request");

        reportCache.removeReportRequest(reportRequest);
        ioReportDao.removeReport(getReportNameFromRequest(reportRequest));

        log.debug("Finish to remove report and report request");
    }
}
