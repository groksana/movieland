package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.GenreService;
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

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenreControllerTest {

    @Mock
    private GenreService mockGenreService;

    @InjectMocks
    private GenreController genreController;

    private MockMvc mockMvc;

    private List<Genre> genres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
        genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setGenre("детектив");
        genres.add(genre);
    }

    @Test
    public void testGetAllGenres() throws Exception {
        when(mockGenreService.getAll()).thenReturn(genres);
        mockMvc.perform(get("/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].genre", is("детектив")));

    }

}
