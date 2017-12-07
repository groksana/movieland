package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    void enrichSingleMovieByGenres(Movie movie);

    void enrichMoviesByGenres(List<Movie> movies);

}
