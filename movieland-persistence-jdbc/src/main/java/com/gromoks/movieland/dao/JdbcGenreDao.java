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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcGenreDao implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();

    private final MovieToGenreRowMapper MOVIE_TO_GENRE_ROW_MAPPER = new MovieToGenreRowMapper();

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

        List<Genre> genres = jdbcTemplate.query(getAllGenreSQL, GENRE_ROW_MAPPER);

        log.info("Finish query to get all genre from db. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }

    @Override
    public List<Genre> getGenreListByMovie(Movie movie) {
        log.debug("Start to get genre list for movie");

        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<Genre> genres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, GENRE_ROW_MAPPER);

        log.debug("Finish to get genre list for movie");
        return genres;
    }

    @Override
    public Map<Integer, List<Genre>> getMovieGenreLink(List<Movie> movies) {
        log.debug("Start to get movie to genre link");

        Map<Integer, List<Genre>> movieGenreMap = new HashMap<>();
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL, namedParameters, MOVIE_TO_GENRE_ROW_MAPPER);
        for (Movie movie : movies) {
            List<Genre> genres = new ArrayList<>();
            for (MovieToGenre movieToGenre : movieToGenres) {
                if (movieToGenre.getMovieId() == movie.getId()) {
                    genres.add(new Genre(movieToGenre.getGenreId(), movieToGenre.getGenre()));
                }
            }
            movieGenreMap.put(movie.getId(), genres);
        }

        log.debug("Finish to get movie to genre link");
        return movieGenreMap;
    }
}
