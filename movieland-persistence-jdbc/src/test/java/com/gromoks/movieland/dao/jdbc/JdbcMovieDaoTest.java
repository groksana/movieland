package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.entity.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcMovieDaoTest {

    @Autowired
    private MovieDao movieDao;

    @Test
    public void testGetAll() {
        List<Movie> movies = movieDao.getAll();
        for (Movie movie : movies) {
            assertNotNull(movie.getId());
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getYearOfRelease());
            assertNotNull(movie.getPrice());
            assertNotNull(movie.getRating());
        }
    }

    @Test
    public void testGetRandom() {
        List<Movie> movies = movieDao.getRandom();
        for (Movie movie : movies) {
            assertNotNull(movie.getId());
            assertNotNull(movie.getNameRussian());
            assertNotNull(movie.getNameNative());
            assertNotNull(movie.getYearOfRelease());
            assertNotNull(movie.getPrice());
            assertNotNull(movie.getRating());
        }
        assertEquals(3, movies.size());
    }

    @Test
    public void testRandomCount() {
        List<Movie> movies = movieDao.getRandom();
        assertEquals(3, movies.size());
    }
}
