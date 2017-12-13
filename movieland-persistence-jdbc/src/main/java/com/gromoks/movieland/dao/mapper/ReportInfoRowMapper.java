package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.entity.ReportInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportInfoRowMapper implements RowMapper<ReportInfo> {
    @Override
    public ReportInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setReportType(resultSet.getString("reportType"));
        reportInfo.setRecipient(resultSet.getString("recipient"));
        reportInfo.setReportLink(resultSet.getString("reportLink"));

        return reportInfo;
    }
}
