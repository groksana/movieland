package com.gromoks.movieland.service.impl.cache;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.service.cache.MovieCache;
import com.gromoks.movieland.service.impl.config.ServiceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {ServiceConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MovieCacheImplTest {

    @Autowired
    private MovieCache movieCache;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEnrichMovieWithRating() {
        Movie movie = new Movie();
        movie.setId(25);
        movie.setNameRussian("Тест");
        movie.setNameNative("Test");
        movie.setYearOfRelease(2016);
        movie.setDescription("Description for Test");
        movie.setPrice(260);
        movie.setPicturePath("https");

        Rating rating = new Rating();
        rating.setMovieId(25);
        rating.setUserId(1);
        rating.setRating(7.8);
        movieCache.addUserMovieRating(rating);
        movieCache.enrichMovieWithRating(movie);

        assertEquals(movie.getRating(),7.8,0);
    }

}
