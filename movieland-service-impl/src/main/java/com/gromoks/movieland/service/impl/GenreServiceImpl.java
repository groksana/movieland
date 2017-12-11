package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.GenreDao;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GenreServiceImpl implements GenreService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("genreCacheImpl")
    private GenreDao genreDao;

    @Autowired
    @Qualifier("jdbcGenreDao")
    private GenreDao genreDaoJdbc;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public void enrichSingleMovieByGenres(Movie movie) {
        log.info("Start to enrich single movie by genres");

        List<Genre> genres = genreDaoJdbc.getGenreListByMovie(movie);
        if (!Thread.currentThread().isInterrupted()) {
            movie.setGenres(genres);
            log.info("Finish to enrich single movie by genres");
        }
    }

    @Override
    public void enrichMoviesByGenres(List<Movie> movies) {
        log.info("Start to enrich movies by genres");

        Map<Integer, List<Genre>> movieToGenres = genreDaoJdbc.getMovieGenreLink(movies);
        for (Movie movie : movies) {
            movie.setGenres(movieToGenres.get(movie.getId()));
        }

        log.info("Finish to enrich movies by genres");
    }
}
