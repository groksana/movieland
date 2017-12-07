package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.GenreDao;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

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
        genreDaoJdbc.enrichSingleMovieByGenres(movie);
    }

    @Override
    public void enrichMoviesByGenres(List<Movie> movies) {
        genreDaoJdbc.enrichMoviesByGenres(movies);
    }
}
