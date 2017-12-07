package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface CountryDao {
    void enrichSingleMovieByCountries(Movie movie);

    void enrichMoviesByCountries(List<Movie> movies);
}
