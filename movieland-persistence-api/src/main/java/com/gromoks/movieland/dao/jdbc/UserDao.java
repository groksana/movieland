package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.User;

import javax.naming.AuthenticationException;

public interface UserDao {
    User getUserByEmailAndPassword(String email, String password) throws AuthenticationException;

}
