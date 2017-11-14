package com.gromoks.movieland.web.controller;


import com.gromoks.movieland.entity.*;
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
import java.util.LinkedHashMap;
import java.util.List;

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
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public void testGetAllMovies() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        when(mockMovieService.getAll(requestParamMap)).thenReturn(movies);

        mockMvc.perform(get("/movie"))
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
    public void testOrderGetAllMoviesPositive() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price","asc");
        when(mockMovieService.getAll(requestParamMap)).thenReturn(movies);

        MvcResult mvcResult = mockMvc.perform(get("/movie?price=asc"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testOrderGetAllMoviesNegative() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("rating","asc");
        when(mockMovieService.getAll(requestParamMap)).thenReturn(movies);

        MvcResult mvcResult = mockMvc.perform(get("/movie?rating=asc"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testGetRandomMovies() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();

        when(mockMovieService.getRandom()).thenReturn(movies);

        mockMvc.perform(get("/movie/random"))
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
                .andExpect(jsonPath("$[0].countries[0].name", is("США")))
                .andExpect(jsonPath("$[0].countries[1].id", is(2)))
                .andExpect(jsonPath("$[0].countries[1].name", is("Украина")))
                .andExpect(jsonPath("$[0].genres[0].id", is(1)))
                .andExpect(jsonPath("$[0].genres[0].name", is("детектив")))
                .andExpect(jsonPath("$[0].genres[1].id", is(2)))
                .andExpect(jsonPath("$[0].genres[1].name", is("драма")));
    }

    @Test
    public void testGetByGenreId() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();

        when(mockMovieService.getByGenreId(1, requestParamMap)).thenReturn(movies);

        mockMvc.perform(get("/movie/genre/1"))
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
    public void testOrderGetByGenreIdPositive() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("price","asc");

        when(mockMovieService.getByGenreId(1, requestParamMap)).thenReturn(movies);

        mockMvc.perform(get("/movie/genre/1?price=asc"))
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
    public void testOrderGetByGenreIdNegative() throws Exception {
        movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNameRussian("Тест");
        movie1.setNameNative("Test");
        movie1.setYearOfRelease(2016);
        movie1.setDescription("Description for Test");
        movie1.setRating(9.8);
        movie1.setPrice(1000000.5);
        movie1.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie1.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie1.setGenres(genres);
        movies.add(movie1);

        LinkedHashMap<String,String> requestParamMap = new LinkedHashMap<>();
        requestParamMap.put("rating","asc");

        when(mockMovieService.getByGenreId(1, requestParamMap)).thenReturn(movies);

        mockMvc.perform(get("/movie/genre/1?rating=asc"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testGetById() throws Exception {
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
        countries.add(new Country(2,"Украина"));
        movie.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie.setGenres(genres);
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1,new User(1,"Robin"),"Cool"));
        reviews.add(new Review(2,new User(2,"Kate"),"The best film"));
        movie.setReviews(reviews);

        when(mockMovieService.getById(1)).thenReturn(movie);

        mockMvc.perform(get("/movie/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nameRussian", is("Тест")))
                .andExpect(jsonPath("$.nameNative", is("Test")))
                .andExpect(jsonPath("$.yearOfRelease", is(2016)))
                .andExpect(jsonPath("$.rating", is(9.8)))
                .andExpect(jsonPath("$.price", is(1000000.5)))
                .andExpect(jsonPath("$.picturePath", is("https")))
                .andExpect(jsonPath("$.description", is("Description for Test")))
                .andExpect(jsonPath("$.countries[0].id", is(1)))
                .andExpect(jsonPath("$.countries[0].name", is("США")))
                .andExpect(jsonPath("$.countries[1].id", is(2)))
                .andExpect(jsonPath("$.countries[1].name", is("Украина")))
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("детектив")))
                .andExpect(jsonPath("$.genres[1].id", is(2)))
                .andExpect(jsonPath("$.genres[1].name", is("драма")))
                .andExpect(jsonPath("$.reviews[0].id", is(1)))
                .andExpect(jsonPath("$.reviews[0].user.id", is(1)))
                .andExpect(jsonPath("$.reviews[0].user.nickname", is("Robin")))
                .andExpect(jsonPath("$.reviews[0].text", is("Cool")))
                .andExpect(jsonPath("$.reviews[1].id", is(2)))
                .andExpect(jsonPath("$.reviews[1].user.id", is(2)))
                .andExpect(jsonPath("$.reviews[1].user.nickname", is("Kate")))
                .andExpect(jsonPath("$.reviews[1].text", is("The best film")));
    }

}
