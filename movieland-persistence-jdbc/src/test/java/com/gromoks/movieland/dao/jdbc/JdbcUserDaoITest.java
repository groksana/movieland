package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.UserDao;
import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.AuthenticationException;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcUserDaoITest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetUserByEmailAndPassword() throws AuthenticationException {
        String email = "gabriel.jackson91@example.com";
        String password = "3e8bbd9caf510ed9a4f047bbed72d853";
        User user = userDao.getUserByEmailAndPassword(email,password);
        assertEquals(user.getNickname(),"Габриэль Джексон");
    }
}
