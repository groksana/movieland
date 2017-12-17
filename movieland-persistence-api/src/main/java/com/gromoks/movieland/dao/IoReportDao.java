package com.gromoks.movieland.dao;

import java.io.InputStream;

public interface IoReportDao {
    InputStream getReport(String filename);

    void removeReport(String filename);
}
