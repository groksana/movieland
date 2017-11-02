package com.gromoks.movieland.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.util.DtoConverter;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/movie", produces = "text/plain;charset=UTF-8")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getAll() throws IOException {

        log.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = movieService.getAll();
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toJson(dtoMovies);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping("/random")
    @ResponseBody
    public String getRandom() throws IOException {

        log.info("Sending request to get 3 random movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = movieService.getRandom();
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toExtendedJson(dtoMovies);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }


}
