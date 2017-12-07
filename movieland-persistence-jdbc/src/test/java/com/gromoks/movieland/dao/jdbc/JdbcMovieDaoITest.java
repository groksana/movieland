package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcMovieDaoITest {

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testGetAll() {
        LinkedHashMap<String, String> requestParamMap = new LinkedHashMap<>();
        List<Movie> movies = movieDao.getAll(requestParamMap);
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getPicturePath());
        }

    }

    @Test
    public void testGetRandom() {
        List<Movie> movies = movieDao.getRandom();
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getPicturePath());
        }
    }

    @Test
    public void testRandomCount() {
        List<Movie> movies = movieDao.getRandom();
        assertEquals(3, movies.size());
    }

    @Test
    public void testGetByGenreId() {
        int genreId = 1;
        LinkedHashMap<String, String> requestParamMap = new LinkedHashMap<>();
        List<Movie> movies = movieDao.getByGenreId(genreId, requestParamMap);
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getPicturePath());
        }
    }

    @Test
    public void testRatingDescOrderGetAll() {
        LinkedHashMap<String, String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("rating", "desc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getRating() <= movies.get(0).getRating());
    }

    @Test
    public void testPriceAscOrderGetAll() {
        LinkedHashMap<String, String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price", "asc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getPrice() >= movies.get(0).getPrice());
    }

    @Test
    public void testPriceDescOrderGetAll() {
        LinkedHashMap<String, String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price", "desc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getPrice() <= movies.get(0).getPrice());
    }

    @Test
    public void testGetById() {
        int movieId = 1;
        Movie movie = movieDao.getById(movieId);
        assertNotNull(movie.getNameRussian());
        assertNotNull(movie.getNameNative());
        assertNotNull(movie.getPicturePath());
    }

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();

        movie.setNameNative("testMovie");
        movie.setNameRussian("тест");
        movie.setRating(9.0);
        movie.setPrice(10000.0);
        movie.setDescription("Cool");
        movie.setPicturePath("http");
        movie.setYearOfRelease(2000);

        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setId(1);
        countries.add(country);
        movie.setCountries(countries);

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre(1);
        genres.add(genre);
        movie.setGenres(genres);

        movieDao.add(movie);

        String getTestDataSql = "SELECT id FROM movie WHERE nameNative = :nameNative";
        MapSqlParameterSource parameterSourceGetTest = new MapSqlParameterSource();
        parameterSourceGetTest.addValue("nameNative", "testMovie");
        int movieId = namedParameterJdbcTemplate.queryForObject(getTestDataSql, parameterSourceGetTest, Integer.class);

        String removeTestDataSql = "DELETE FROM movie WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", movieId);
        int updateCount = namedParameterJdbcTemplate.update(removeTestDataSql, parameterSource);
        assertEquals(1, updateCount);

        removeTestDataSql = "DELETE FROM movie2country WHERE movieId = :movieId";
        MapSqlParameterSource parameterSourceMovie2Country = new MapSqlParameterSource();
        parameterSourceMovie2Country.addValue("movieId", movieId);
        updateCount = namedParameterJdbcTemplate.update(removeTestDataSql, parameterSourceMovie2Country);
        assertEquals(1, updateCount);

        removeTestDataSql = "DELETE FROM movie2genre WHERE movieId = :movieId";
        MapSqlParameterSource parameterSourceMovie2Genre = new MapSqlParameterSource();
        parameterSourceMovie2Genre.addValue("movieId", movieId);
        updateCount = namedParameterJdbcTemplate.update(removeTestDataSql, parameterSourceMovie2Genre);
        assertEquals(1, updateCount);
    }

}
