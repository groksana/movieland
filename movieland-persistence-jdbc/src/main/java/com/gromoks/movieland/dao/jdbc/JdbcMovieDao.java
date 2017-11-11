package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.gromoks.movieland.dao.jdbc.mapper.MovieToCountryRowMapper;
import com.gromoks.movieland.dao.jdbc.mapper.MovieToGenreRowMapper;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BeanPropertyRowMapper<MovieToCountry> movieToCountryBeanPropertyRowMapper = new BeanPropertyRowMapper<>(MovieToCountry.class);

    private final MovieRowMapper movieRowMapper = new MovieRowMapper();

    private final MovieToCountryRowMapper movieToCountryRowMapper = new MovieToCountryRowMapper();

    private final MovieToGenreRowMapper movieToGenreRowMapper = new MovieToGenreRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllMovieSQL;

    @Autowired
    private String getAllMovieToCountrySQL;

    @Autowired
    private String getAllMovieToGenreSQL;

    @Autowired
    private String getRandomMovieSQL;

    @Autowired
    private String getMoviesByGenreIdSQL;

    public List<Movie> getAll(HashMap<String,String> requestParamMap) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(getAllMovieSQL);

        int rowNumber = 0;

        for (Map.Entry<String,String> entry : requestParamMap.entrySet()) {
            if (entry.getValue() != null) {
                if (rowNumber == 0) {
                    sqlBuilder.append(" ORDER BY ");
                }
                rowNumber++;
                if (rowNumber > 1) {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(entry.getKey());
                sqlBuilder.append(" ");
                sqlBuilder.append(entry.getValue());
            }
        }
        String resultQuery = sqlBuilder.toString();

        log.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies  = jdbcTemplate.query(resultQuery, movieRowMapper);
        log.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getRandom() {
        log.info("Start query to get 3 random movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies  = jdbcTemplate.query(getRandomMovieSQL, movieRowMapper);
        for (Movie movie : movies) {
            enrichMovieWithCountry(movie, getMovieToCountryList());
            enrichMovieWithGenre(movie, getMovieToGenreList());
        }
        log.info("Finish query to get 3 random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getByGenreId(int id, HashMap<String,String> requestParamMap) {
        log.info("Start query to get movies by genre");
        long startTime = System.currentTimeMillis();
        String resultQuery = enrichQueryWithOrderRequestParam(getMoviesByGenreIdSQL, requestParamMap);
        List<Movie> movies  = jdbcTemplate.query(resultQuery,movieRowMapper,id);
        log.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    private List<MovieToCountry> getMovieToCountryList() {
        List<MovieToCountry> movieToCountries = jdbcTemplate.query(getAllMovieToCountrySQL,
                movieToCountryRowMapper);
        return movieToCountries;
    }

    private List<MovieToGenre> getMovieToGenreList() {
        List<MovieToGenre> movieToGenres = jdbcTemplate.query(getAllMovieToGenreSQL,
                movieToGenreRowMapper);
        return movieToGenres;
    }

    private void enrichMovieWithCountry(Movie movie, List<MovieToCountry> movieToCountries) {
        List<Country> countries = new ArrayList<>();
        for (MovieToCountry movieToCountry : movieToCountries) {
            if (movieToCountry.getMovieId() == movie.getId()) {
                countries.add(new Country(movieToCountry.getCountryId(), movieToCountry.getCountry()));
            }
        }
        movie.setCountries(countries);
    }

    private void enrichMovieWithGenre(Movie movie, List<MovieToGenre> movieToGenres) {
        List<Genre> genres = new ArrayList<>();
        for (MovieToGenre movieToGenre : movieToGenres) {
            if (movieToGenre.getMovieId() == movie.getId()) {
                genres.add(new Genre(movieToGenre.getGenreId(), movieToGenre.getGenre()));
            }
        }
        movie.setGenres(genres);
    }

    private String enrichQueryWithOrderRequestParam(String initialQuery, HashMap<String,String> requestParamMap) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(initialQuery);
        int rowNumber = 0;

        for (Map.Entry<String,String> entry : requestParamMap.entrySet()) {
            if (entry.getValue() != null) {
                if (rowNumber == 0) {
                    sqlBuilder.append(" ORDER BY ");
                }
                rowNumber++;
                if (rowNumber > 1) {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(entry.getKey());
                sqlBuilder.append(" ");
                sqlBuilder.append(entry.getValue());
            }
        }

        String resultQuery = sqlBuilder.toString();
        return resultQuery;
    }

}
