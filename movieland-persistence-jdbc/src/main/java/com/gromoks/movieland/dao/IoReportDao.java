package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.ReportMovie;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

@Repository
public class IoReportDao implements ReportDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${report.directory}")
    private String reportDirectory;

    @Autowired
    private ReportDao reportDao;

    @Override
    public File getFile(String filename) {
        log.info("Start to get file {}", filename);

        File file = new File(reportDirectory + "/" + filename);
        if (!file.exists()) {
            log.error("File with path: {}/{} was not found.", reportDirectory, filename);
            throw new RuntimeException("File with path: " + reportDirectory + "/" + filename + " was not found.");
        }

        log.info("Finish to get file {}", filename);
        return file;
    }

    @Override
    public List<ReportMovie> getAllReportMovie() {
        return reportDao.getAllReportMovie();
    }

    @Override
    public void insertReportInfo(ReportInfo reportInfo) {
        reportDao.insertReportInfo(reportInfo);
    }

    @Override
    public List<ReportInfo> getReportLinkByEmail(String email) {
        return reportDao.getReportLinkByEmail(email);
    }

    @Override
    public void removeFile(String filename) {
        log.info("Start to remove file with name {}", filename);

        File file = new File(reportDirectory + "/" + filename);

        if (file.exists()) {
            file.delete();
            log.info("Finish to remove file with name {}", filename);
        } else {
            log.error("No such file {}", filename);
            throw new RuntimeException("No such file " + filename);
        }
    }

    @Scheduled(cron = "${report.clean.cron}")
    private void cleanReportDirectory() {
        log.info("Start to clean report directory: {}", reportDirectory);

        try {
            FileUtils.cleanDirectory(new File(reportDirectory + "/"));
        } catch (IOException e) {
            log.error("Directory doesn't exists: {}", reportDirectory);
        }

        log.info("Finish to clean report directory");
    }
}
