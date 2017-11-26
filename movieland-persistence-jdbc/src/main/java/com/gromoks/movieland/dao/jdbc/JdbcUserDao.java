package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.jdbc.mapper.UserRowMapper;
import com.gromoks.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.AuthenticationException;

@Repository
public class JdbcUserDao implements UserDao{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getUserByEmailAndPasswordSQL;

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws AuthenticationException{
        log.info("Start query to get user by email and password from db");
        long startTime = System.currentTimeMillis();
        try {
            User user = jdbcTemplate.queryForObject(getUserByEmailAndPasswordSQL, userRowMapper, new Object[]{email, password});
            log.info("Finish query to get user by email and password from db. It took {} ms", System.currentTimeMillis() - startTime);
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Requested user doesn't exists or password is incorrect. Email: {}",email);
            throw new AuthenticationException("Requested user doesn't exists or password is incorrect. Email: " + email);
        }
    }
}
