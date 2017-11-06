package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getRandom();

    List<Movie> getByGenreId(int id);
}
