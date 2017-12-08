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

    private final MovieToCountryRowMapper MOVIE_TO_COUNTRY_ROW_MAPPER = new MovieToCountryRowMapper();

    private final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getAllMovieToCountrySQL;

    @Override
    public List<MovieToCountry> getMovieToCountryList(List<Movie> movies) {
        log.debug("Start to get movie to country link");

        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, MOVIE_TO_COUNTRY_ROW_MAPPER);

        log.debug("Finish to get movie to country link");
        return movieToCountries;
    }

    @Override
    public List<Country> getCountryListByMovie(Movie movie) {
        log.debug("Start to get country list for movie");

        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<Country> countries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL, namedParameters, COUNTRY_ROW_MAPPER);

        log.debug("Finish to get country list for movie");
        return countries;
    }
}
