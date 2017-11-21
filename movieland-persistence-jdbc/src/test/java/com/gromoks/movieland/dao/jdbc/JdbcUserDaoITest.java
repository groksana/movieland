package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcUserDaoITest {
    @Autowired
    UserDao userDao;

    @Test
    public void testGetUserByEmail() {
        String email = "gabriel.jackson91@example.com";
        User user = userDao.getUserByEmail(email);
        assertEquals(user.getNickname(),"Габриэль Джексон");
    }
}
