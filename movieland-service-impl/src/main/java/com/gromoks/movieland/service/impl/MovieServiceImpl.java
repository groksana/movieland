package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.service.*;
import com.gromoks.movieland.service.cache.MovieCache;
import com.gromoks.movieland.service.concurrent.ConcurrentEnrichmentMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MovieCache movieCache;

    @Autowired
    private CountryService countryService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ConcurrentEnrichmentMovieService concurrentEnrichmentMovieService;

    public MovieServiceImpl() {
    }

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
        countryService.enrichMoviesByCountries(movies);
        genreService.enrichMoviesByGenres(movies);

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
        log.info("Start to get movie by id = {}", id);

        Movie cachedMovie = movieCache.getById(id);
        Movie movie;
        if (cachedMovie == null) {
            log.debug("Movie doesn't not exist in cache");
            Movie dbMovie = movieDao.getById(id);
            concurrentEnrichmentMovieService.enrichMovie(dbMovie);
            movieCache.add(dbMovie);
            movie = new Movie(dbMovie);
        } else {
            movie = new Movie(cachedMovie);
        }
        enrichMovieWithRating(movie);

        log.info("Finish to get movie by id = {}", id);
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
        movieDao.add(movie);
    }

    @Override
    public void edit(Movie movie) {
        movieDao.edit(movie);
        movieCache.removeById(movie.getId());
    }
}

