package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.User;

public interface UserDao {
    User getUserByEmail(String email);
}
