package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.dao.mapper.CountryRowMapper;
import com.gromoks.movieland.dao.mapper.MovieToCountryRowMapper;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcCountryDao implements CountryDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MovieToCountryRowMapper movieToCountryRowMapper = new MovieToCountryRowMapper();

    private final CountryRowMapper countryRowMapper = new CountryRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getAllMovieToCountrySQL;

    @Override
    public void enrichSingleMovieByCountries(Movie movie) {
        log.info("Start to enrich single movie by countries");

        List<Country> countries = getCountryListByMovie(movie);

        if (!Thread.currentThread().isInterrupted()) {
            movie.setCountries(countries);
            log.info("Finish to enrich single movie by countries");
        }
    }

    @Override
    public void enrichMoviesByCountries(List<Movie> movies) {
        log.info("Start to enrich movies by countries");

        List<MovieToCountry> movieToCountries = getMovieToCountryList(movies);
        for (Movie movie : movies) {
            List<Country> countries = new ArrayList<>();
            for (MovieToCountry movieToCountry : movieToCountries) {
                if (movieToCountry.getMovieId() == movie.getId()) {
                    countries.add(new Country(movieToCountry.getCountryId(), movieToCountry.getCountry()));
                }
            }
            movie.setCountries(countries);
        }

        log.info("Finish to enrich movies by countries");
    }

    private List<MovieToCountry> getMovieToCountryList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, movieToCountryRowMapper);

        return movieToCountries;
    }

    private List<Country> getCountryListByMovie(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<Country> countries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, countryRowMapper);

        return countries;
    }
}
