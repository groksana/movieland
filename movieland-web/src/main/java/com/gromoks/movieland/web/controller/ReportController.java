package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.ReportService;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;
import com.gromoks.movieland.service.entity.UserRole;
import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.web.security.Protected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/report")
public class ReportController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ReportService reportService;

    @ResponseBody
    @Protected(UserRole.ADMIN)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addReportRequest(@RequestBody ReportRequest reportRequest, HttpServletRequest request) {
        log.info("Sending request to generate report");
        long startTime = System.currentTimeMillis();

        User user = authenticationService.getAuthenticatedUser();
        reportRequest.setRequestedUser(user);
        reportRequest.setRequestUuid(UUID.randomUUID().toString());
        reportRequest.setStatus(ReportStatus.NEW);
        reportRequest.setRequestUrl(request.getRequestURL().toString());
        reportService.addReportRequest(reportRequest);

        log.info("Request for report has been added. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @Protected(UserRole.ADMIN)
    @RequestMapping(value = "/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws IOException {
        log.info("Sending request to get report");
        long startTime = System.currentTimeMillis();

        byte[] document = reportService.getReport(filename);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "octet-stream"));
        header.set("Content-Disposition", "inline; filename=" + filename);
        header.setContentLength(document.length);

        log.info("Report has been got. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(document, header, HttpStatus.OK);
    }

    @ResponseBody
    @Protected(UserRole.ADMIN)
    @RequestMapping(value = "/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getReportStatusByUser() {
        log.info("Sending request to get report status by user");
        long startTime = System.currentTimeMillis();

        User user = authenticationService.getAuthenticatedUser();
        List<ReportRequest> reportRequests = reportService.getReportRequestStatusByUser(user);

        log.info("Report status has been got. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(reportRequests, HttpStatus.OK);
    }

    @ResponseBody
    @Protected(UserRole.ADMIN)
    @RequestMapping(value = "/link", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getReportLinkByEmail() {
        log.info("Sending request to get report link by user");
        long startTime = System.currentTimeMillis();

        User user = authenticationService.getAuthenticatedUser();
        List<ReportInfo> reportInfos = reportService.getReportLinkByEmail(user.getEmail());

        log.info("Report links have been got. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(reportInfos, HttpStatus.OK);
    }
}
