package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcMovieDaoTest {

    @Autowired
    private MovieDao movieDao;

    @Test
    public void testGetAll() {
        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
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
            assertNotNull(movie.getCountries());
            assertNotNull(movie.getGenres());
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
        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        List<Movie> movies = movieDao.getByGenreId(genreId,requestParamMap);
        for (Movie movie : movies) {
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getPicturePath());
        }
    }

    @Test
    public void testRatingDescOrderGetAll() {
        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("rating","desc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getRating()<=movies.get(0).getRating());
    }

    @Test
    public void testPriceAscOrderGetAll() {
        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price","asc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getPrice()>=movies.get(0).getPrice());
    }

    @Test
    public void testPriceDescOrderGetAll() {
        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price","desc");
        List<Movie> movies = movieDao.getAll(requestParamMap);
        assertTrue(movies.get(1).getPrice()<=movies.get(0).getPrice());
    }

}
