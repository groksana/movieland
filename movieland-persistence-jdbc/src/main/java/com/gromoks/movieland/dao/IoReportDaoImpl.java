package com.gromoks.movieland.dao;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import java.io.*;

@Repository
public class IoReportDaoImpl implements IoReportDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${report.directory}")
    private String reportDirectory;

    @Override
    public InputStream getReport(String filename) {
        log.info("Start to get report {}", filename);

        File file = new File(reportDirectory, filename);
        InputStream inputStream;

        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("File with path: {} was not found.", file.getName());
            throw new RuntimeException("File with path: " + file.getName() + " was not found.");
        }

        log.info("Finish to get report {}", filename);
        return inputStream;
    }

    @Override
    public void removeReport(String filename) {
        log.info("Start to remove file with name {}", filename);

        File file = new File(reportDirectory, filename);

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
            FileUtils.cleanDirectory(new File(reportDirectory));
        } catch (IOException e) {
            log.error("Directory doesn't exists: {}", reportDirectory);
        }

        log.info("Finish to clean report directory");
    }
}
