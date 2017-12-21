package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.InfoReportDao;
import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.ReportInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcInfoReportDaoITest {

    @Autowired
    private InfoReportDao infoReportDao;

    @Test
    public void testGetUserByEmailAndPassword() {
        String email = "travis.wright36@example.com";

        List<ReportInfo> reportInfos = infoReportDao.getReportLinkByEmail(email);

        assertEquals(reportInfos.get(0).getRecipient(),email);
    }
}
