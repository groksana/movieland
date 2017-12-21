package com.gromoks.movieland.dao;

import java.io.InputStream;

public interface DataReportDao {
    InputStream getReport(String filename);

    void removeReport(String filename);
}
