package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {
    List<Movie> getAll();

    List<Movie> getRandom();
}
