package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.service.cache.MovieCache;
import com.gromoks.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MovieCache movieCache;

    public MovieServiceImpl() {}

    public MovieServiceImpl(MovieCache movieCache) {
        this.movieCache = movieCache;
    }

    @Override
    public List<Movie> getAll(LinkedHashMap<String, String> requestParamMap) {

        List<Movie> movies = movieDao.getAll(requestParamMap);
        for (Movie movie : movies) {
            enrichMovieWithRating(movie);
        }

        return movies;
    }

    @Override
    public List<Movie> getRandom() {

        List<Movie> movies = movieDao.getRandom();
        for (Movie movie : movies) {
            enrichMovieWithRating(movie);
        }

        return movies;
    }

    @Override
    public List<Movie> getByGenreId(int id, LinkedHashMap<String, String> requestParamMap) {

        List<Movie> movies = movieDao.getByGenreId(id, requestParamMap);
        for (Movie movie : movies) {
            enrichMovieWithRating(movie);
        }

        return movies;
    }

    @Override
    public Movie getById(int id) {

        Movie movie = movieCache.getById(id);
        enrichMovieWithRating(movie);

        return movie;
    }

    @Override
    public void addMovieRating(Rating rating) {
        movieCache.addUserMovieRating(rating);
    }

    @Override
    public void enrichMovieWithRating(Movie movie) {
        movieCache.enrichMovieWithRating(movie);
    }

    @Override
    public void add(Movie movie) {
        int movieId = movieDao.add(movie);
        movieCache.removeById(movieId);
    }

    @Override
    public void edit(Movie movie) {
        movieDao.edit(movie);
        movieCache.removeById(movie.getId());
    }
}

