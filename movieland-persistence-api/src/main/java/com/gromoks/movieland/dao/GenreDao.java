package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;

import java.util.List;
import java.util.Map;

public interface GenreDao {
    List<Genre> getAll();

    List<Genre> getGenreListByMovie(Movie movie);

    Map<Integer, List<Genre>> getMovieGenreLink(List<Movie> movies);

}
