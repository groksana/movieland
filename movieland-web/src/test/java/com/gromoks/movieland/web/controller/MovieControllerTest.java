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
        mockMvc.perform(get("/movie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nameRussian", is("Тест")))
                .andExpect(jsonPath("$[0].nameNative", is("Test")));
    }

    @Test
    public void testGetRandomMovies() throws Exception {
        when(mockMovieService.getRandom()).thenReturn(movies);
        MvcResult mvcResult = mockMvc.perform(get("/movie/random"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree = mapper.readTree(json);

        assertEquals(tree.get(0).get("id").asInt(), 1);
        assertEquals(tree.get(0).get("countries").get(0).get("countryId").asInt(), 1);
        assertEquals(tree.get(0).get("countries").get(0).get("country").asText(), "США");
        assertEquals(tree.get(0).get("genres").get(0).get("genreId").asInt(), 1);
        assertEquals(tree.get(0).get("genres").get(0).get("genre").asText(), "детектив");
    }

}
