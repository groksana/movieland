package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.ReportMovie;

import java.io.File;
import java.util.List;

public interface ReportDao {
    File getFile(String filename);

    List<ReportMovie> getAllReportMovie();

    void insertReportInfo(ReportInfo reportInfo);

    List<ReportInfo> getReportLinkByEmail(String email);
}
