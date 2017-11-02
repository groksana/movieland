package com.gromoks.movieland.web.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MovieControllerTest {

    @Mock
    private MovieService mockMovieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private List<Movie> movies;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
        movies = new ArrayList<>();
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNameRussian("Тест");
        movie.setNameNative("Test");
        movie.setYearOfRelease(2016);
        movie.setDescription("Description for Test");
        movie.setRating(9.8);
        movie.setPrice(1000000.5);
        movie.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        movie.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        movie.setGenres(genres);
        movies.add(movie);
    }

    @Test
    public void testGetAllMovies() throws Exception {
        when(mockMovieService.getAll()).thenReturn(movies);
        mockMvc.perform(get("/v1/movie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nameRussian", is("Тест")))
                .andExpect(jsonPath("$[0].nameNative", is("Test")))
                .andExpect(jsonPath("$[0].yearOfRelease", is(2016)))
                .andExpect(jsonPath("$[0].rating", is(9.8)))
                .andExpect(jsonPath("$[0].price", is(1000000.5)))
                .andExpect(jsonPath("$[0].picturePath", is("https")));
    }

    @Test
    public void testGetRandomMovies() throws Exception {
        when(mockMovieService.getRandom()).thenReturn(movies);
        mockMvc.perform(get("/v1/movie/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nameRussian", is("Тест")))
                .andExpect(jsonPath("$[0].nameNative", is("Test")))
                .andExpect(jsonPath("$[0].yearOfRelease", is(2016)))
                .andExpect(jsonPath("$[0].rating", is(9.8)))
                .andExpect(jsonPath("$[0].price", is(1000000.5)))
                .andExpect(jsonPath("$[0].picturePath", is("https")))
                .andExpect(jsonPath("$[0].description", is("Description for Test")))
                .andExpect(jsonPath("$[0].countries[0].id", is(1)))
                .andExpect(jsonPath("$[0].countries[0].country", is("США")))
                .andExpect(jsonPath("$[0].genres[0].id", is(1)))
                .andExpect(jsonPath("$[0].genres[0].genre", is("детектив")));
    }


}
