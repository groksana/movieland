package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.mapper.ReportInfoRowMapper;
import com.gromoks.movieland.dao.mapper.ReportMovieRowMapper;
import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.ReportMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.util.List;

@Repository
public class JdbcReportDao implements ReportDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ReportMovieRowMapper REPORT_MOVIE_ROW_MAPPER = new ReportMovieRowMapper();

    private final ReportInfoRowMapper REPORT_INFO_ROW_MAPPER = new ReportInfoRowMapper();

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String insertReportInfoSQL;

    @Autowired
    private String getAllReportMovieSQL;

    @Autowired
    private String getReportLinkByEmailSQL;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public File getFile(String filename) {
        return reportDao.getFile(filename);
    }

    @Override
    public List<ReportMovie> getAllReportMovie() {
        log.info("Start query to get all movies for report from DB");
        long startTime = System.currentTimeMillis();

        List<ReportMovie> reportMovies = jdbcTemplate.query(getAllReportMovieSQL, REPORT_MOVIE_ROW_MAPPER);

        log.info("Finish query to get all movies for report from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return reportMovies;

    }

    @Override
    public void insertReportInfo(ReportInfo reportInfo) {
        log.info("Start query to add report info {} to DB", reportInfo);
        long startTime = System.currentTimeMillis();

        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            parameterSource.addValue("reportType", reportInfo.getReportType());
            parameterSource.addValue("recipient", reportInfo.getRecipient());
            parameterSource.addValue("reportLink", reportInfo.getReportLink());

            namedParameterJdbcTemplate.update(insertReportInfoSQL, parameterSource);

            transactionManager.commit(transactionStatus);

            log.info("Finish query to add report info {} to DB. It took {} ms", reportInfo, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new RuntimeException("Operation has not been done");
        }

    }

    @Override
    public List<ReportInfo> getReportLinkByEmail(String email) {
        log.info("Start query to get report link from db");
        long startTime = System.currentTimeMillis();

        List<ReportInfo> reportInfos = jdbcTemplate.query(getReportLinkByEmailSQL, REPORT_INFO_ROW_MAPPER, email);

        log.info("Finish query to get report link by email from db. It took {} ms", System.currentTimeMillis() - startTime);
        return reportInfos;
    }
}
