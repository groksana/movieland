package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.dao.mapper.GenreRowMapper;
import com.gromoks.movieland.dao.mapper.MovieToGenreRowMapper;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcGenreDao implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GenreRowMapper genreRowMapper = new GenreRowMapper();

    private final MovieToGenreRowMapper movieToGenreRowMapper = new MovieToGenreRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getAllGenreSQL;

    @Autowired
    private String getAllMovieToGenreSQL;

    @Override
    public List<Genre> getAll() {
        log.info("Start query to get all genre from db");
        long startTime = System.currentTimeMillis();

        List<Genre> genres = jdbcTemplate.query(getAllGenreSQL, genreRowMapper);

        log.info("Finish query to get all genre from db. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }

    @Override
    public void enrichSingleMovieByGenres(Movie movie) {
        log.info("Start to enrich single movie by genres");

        List<Genre> genres = getGenreListByMovie(movie);
        if (!Thread.currentThread().isInterrupted()) {
            movie.setGenres(genres);
            log.info("Finish to enrich single movie by genres");
        }
    }

    @Override
    public void enrichMoviesByGenres(List<Movie> movies) {
        log.info("Start to enrich movies by genres");

        List<MovieToGenre> movieToGenres = getMovieToGenreList(movies);
        for (Movie movie : movies) {
            List<Genre> genres = new ArrayList<>();
            for (MovieToGenre movieToGenre : movieToGenres) {
                if (movieToGenre.getMovieId() == movie.getId()) {
                    genres.add(new Genre(movieToGenre.getGenreId(), movieToGenre.getGenre()));
                }
            }
            movie.setGenres(genres);
        }

        log.info("Finish to enrich movies by genres");
    }

    private List<Genre> getGenreListByMovie(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<Genre> genres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, genreRowMapper);

        return genres;
    }

    private List<MovieToGenre> getMovieToGenreList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, movieToGenreRowMapper);

        return movieToGenres;
    }
}
