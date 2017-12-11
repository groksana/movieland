package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    void enrichSingleMovieByGenres(Movie movie);
    void enrichMoviesByGenres(List<Movie> movies);

}
