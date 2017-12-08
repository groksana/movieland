package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    List<Genre> getGenreListByMovie(Movie movie);

    List<MovieToGenre> getMovieToGenreList(List<Movie> movies);

}
