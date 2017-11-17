package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.dao.entity.MovieToReview;
import com.gromoks.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.gromoks.movieland.dao.jdbc.mapper.MovieToCountryRowMapper;
import com.gromoks.movieland.dao.jdbc.mapper.MovieToGenreRowMapper;
import com.gromoks.movieland.dao.jdbc.mapper.MovieToReviewRowMapper;
import com.gromoks.movieland.dao.jdbc.sqlbuilder.QueryBuilder;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BeanPropertyRowMapper<MovieToCountry> movieToCountryBeanPropertyRowMapper = new BeanPropertyRowMapper<>(MovieToCountry.class);

    private final MovieRowMapper movieRowMapper = new MovieRowMapper();

    private final MovieToCountryRowMapper movieToCountryRowMapper = new MovieToCountryRowMapper();

    private final MovieToGenreRowMapper movieToGenreRowMapper = new MovieToGenreRowMapper();

    private final MovieToReviewRowMapper movieToReviewRowMapper = new MovieToReviewRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getAllMovieSQL;

    @Autowired
    private String getAllMovieToCountrySQL;

    @Autowired
    private String getAllMovieToGenreSQL;

    @Autowired
    private String getMovieToReviewSQL;

    @Autowired
    private String getRandomMovieSQL;

    @Autowired
    private String getMoviesByGenreIdSQL;

    @Autowired
    private String getMoviesByIdSQL;

    public List<Movie> getAll(LinkedHashMap<String,String> requestParamMap) {
        log.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();
        String resultQuery = QueryBuilder.enrichQueryWithOrderRequestParam(getAllMovieSQL, requestParamMap);
        List<Movie> movies  = jdbcTemplate.query(resultQuery, movieRowMapper);
        log.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getRandom() {
        log.info("Start query to get 3 random movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies  = jdbcTemplate.query(getRandomMovieSQL, movieRowMapper);
        List<MovieToCountry> movieToCountries = getMovieToCountryList(movies);
        List<MovieToGenre> movieToGenres = getMovieToGenreList(movies);
        for (Movie movie : movies) {
            enrichMovieWithCountry(movie, movieToCountries);
            enrichMovieWithGenre(movie, movieToGenres);
        }
        log.info("Finish query to get 3 random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getByGenreId(int id, LinkedHashMap<String,String> requestParamMap) {
        log.info("Start query to get movies by genre");
        long startTime = System.currentTimeMillis();
        String resultQuery = QueryBuilder.enrichQueryWithOrderRequestParam(getMoviesByGenreIdSQL, requestParamMap);
        List<Movie> movies  = jdbcTemplate.query(resultQuery,movieRowMapper,id);
        log.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public Movie getById(int id) {
        log.info("Start query to get movies by id");
        long startTime = System.currentTimeMillis();
        Movie movie  = jdbcTemplate.queryForObject(getMoviesByIdSQL, movieRowMapper, id);
        List<MovieToCountry> movieToCountries = getSingleMovieToCountryList(movie);
        List<MovieToGenre> movieToGenres = getSingleMovieToGenreList(movie);
        List<MovieToReview> movieToReviews = getSingleMovieToReviewList(movie);
        enrichMovieWithCountry(movie, movieToCountries);
        enrichMovieWithGenre(movie, movieToGenres);
        enrichMovieWithReview(movie, movieToReviews);
        log.info("Finish query to get movies by id from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;
    }

    private List<MovieToCountry> getMovieToCountryList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }
        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL,namedParameters,movieToCountryRowMapper);
        return movieToCountries;
    }

    private List<MovieToCountry> getSingleMovieToCountryList(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());
        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToCountry> movieToCountries = namedParameterJdbcTemplate.query(getAllMovieToCountrySQL,namedParameters,movieToCountryRowMapper);
        return movieToCountries;
    }

    private List<MovieToGenre> getMovieToGenreList(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
        }
        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL,namedParameters,movieToGenreRowMapper);
        return movieToGenres;
    }

    private List<MovieToGenre> getSingleMovieToGenreList(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());
        SqlParameterSource namedParameters = new MapSqlParameterSource("ids", movieIds);
        List<MovieToGenre> movieToGenres = namedParameterJdbcTemplate.query(getAllMovieToGenreSQL,namedParameters,movieToGenreRowMapper);
        return movieToGenres;
    }

    private List<MovieToReview> getSingleMovieToReviewList(Movie movie) {
        List<MovieToReview> movieToReviews  = jdbcTemplate.query(getMovieToReviewSQL, movieToReviewRowMapper, movie.getId());
        return movieToReviews;
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

    private void enrichMovieWithReview(Movie movie, List<MovieToReview> movieToReviews) {
        List<Review> reviews = new ArrayList<>();
        for (MovieToReview movieToReview : movieToReviews) {
            if (movieToReview.getMovieId() == movie.getId()) {
                reviews.add(new Review(movieToReview.getId(),movieToReview.getUser(),movieToReview.getText()));
            }
        }
        movie.setReviews(reviews);
    }

}
