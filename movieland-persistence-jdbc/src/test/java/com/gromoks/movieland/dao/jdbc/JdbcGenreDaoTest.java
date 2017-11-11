package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.Genre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath:spring/jdbc-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcGenreDaoTest {

    @Autowired
    @Qualifier("jdbcGenreDao")
    private GenreDao genreDao;



    @Test
    public void testGetAll() {
        List<Genre> genres = genreDao.getAll();
        for (Genre genre : genres) {
            assertNotNull(genre.getName());
        }

    }


}
