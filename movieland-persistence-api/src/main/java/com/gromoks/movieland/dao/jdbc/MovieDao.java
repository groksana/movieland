package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.Movie;

import java.util.HashMap;
import java.util.List;

public interface MovieDao {
    List<Movie> getAll(HashMap<String,String> requestParamMap);

    List<Movie> getRandom();

    List<Movie> getByGenreId(int id, HashMap<String,String> requestParamMap);
}
