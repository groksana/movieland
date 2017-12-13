package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.ReportMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Repository
public class IoReportDao implements ReportDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportDao reportDao;

    @Override
    public File getFile(String filename) {
        log.info("Start to get file {}", filename);

        File file = new File("reports/" + filename);
        if (!file.exists()) {
            log.error("File with path: reports/{} was not found.", filename);
            throw new RuntimeException("File with path: " + "reports/" + filename + " was not found.");
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
}
