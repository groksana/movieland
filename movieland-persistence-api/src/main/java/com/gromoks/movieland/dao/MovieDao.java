package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.CachedMovieRating;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface MovieDao {
    List<Movie> getAll(LinkedHashMap<String,String> requestParamMap);

    List<Movie> getRandom();

    List<Movie> getByGenreId(int id, LinkedHashMap<String,String> requestParamMap);

    Movie getById(int id);

    void addMovieRatings(ConcurrentLinkedQueue<Rating> ratings);

    List<CachedMovieRating> getMovieRating();

    int add(Movie movie);

    void edit(Movie movie);
}
