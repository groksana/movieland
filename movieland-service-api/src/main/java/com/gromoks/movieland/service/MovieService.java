package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;

import java.util.LinkedHashMap;
import java.util.List;

public interface MovieService {
    List<Movie> getAll(LinkedHashMap<String,String> requestParamMap);

    List<Movie> getRandom();

    List<Movie> getByGenreId(int id, LinkedHashMap<String,String> requestParamMap);

    Movie getById(int id);
}
