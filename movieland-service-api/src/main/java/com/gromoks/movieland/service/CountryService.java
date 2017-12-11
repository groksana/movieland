package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface CountryService {
    void enrichSingleMovieByCountries(Movie movie);

    void enrichMoviesByCountries(List<Movie> movies);
}
