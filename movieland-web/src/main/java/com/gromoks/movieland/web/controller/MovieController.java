package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.util.DtoConverter;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/movie", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieService movieService;

    @RequestMapping
    public String getAll(@RequestParam(value = "rating", required = false) String rating,
                         @RequestParam(value = "price", required = false) String price) {
        log.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();
        HashMap<String,String> requestParamMap = new HashMap<>();
        if (rating != null) { requestParamMap.put("rating", rating); }
        if (price != null) { requestParamMap.put("price", price); }
        List<Movie> movies = movieService.getAll(requestParamMap);
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toJsonNormalMovie(dtoMovies);
        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/random")
    public String getRandom() {
        log.info("Sending request to get 3 random movies");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getRandom();
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toJsonExtendedMovie(dtoMovies);
        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/genre/{genreId}")
    public String getByGenreId(@PathVariable int genreId) {
        log.info("Sending request to get movies by genre with id = {}", genreId);
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieService.getByGenreId(genreId);
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toJsonNormalMovie(dtoMovies);
        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }


}
