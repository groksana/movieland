package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.web.entity.*;
import com.gromoks.movieland.web.util.DtoConverter;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/movie", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieService movieService;

    @RequestMapping
    public String getAll(@RequestParam(required = false) LinkedHashMap<String, String> requestParamMap) {
        log.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();
        validateMovieRequest(requestParamMap);
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
    public String getByGenreId(@PathVariable int genreId, @RequestParam(required = false) LinkedHashMap<String, String> requestParamMap) {
        log.info("Sending request to get movies by genre with id = {}", genreId);
        long startTime = System.currentTimeMillis();
        validateMovieRequest(requestParamMap);
        List<Movie> movies = movieService.getByGenreId(genreId, requestParamMap);
        List<MovieDto> dtoMovies = DtoConverter.toMovieDtoList(movies);
        String json = JsonJacksonConverter.toJsonNormalMovie(dtoMovies);
        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

<<<<<<< HEAD
    private void validateMovieRequest(LinkedHashMap<String, String> requestParamMap) {
        for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
            RequestParameter requestParameter = RequestParameter.getByName(entry.getKey());
            SortingOrder sortingOrder = SortingOrder.getByName(entry.getValue());

            if ((requestParameter == requestParameter.RATING && sortingOrder == SortingOrder.DESC)
                || (requestParameter == requestParameter.PRICE && sortingOrder != null)) {

            } else {
                throw new IllegalArgumentException("Exception with illegal argument: " + requestParameter + "=" + sortingOrder);
            }
        }
    }
}
=======
    private void validateMovieRequest(HashMap<String, String> requestParamMap) {
        for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
            Enum key = RequestParameter.getByRequestParamName(entry.getKey());
            Enum value = SortingOrder.getBySortingOrderName(entry.getValue());
            if ((!key.equals(RequestParameter.PRICE) && !key.equals(RequestParameter.RATING))
                    || (key.equals(RequestParameter.RATING) && !value.equals(SortingOrder.DESC))
                    || (key.equals(RequestParameter.PRICE) && !(value.equals(SortingOrder.DESC) || value.equals(SortingOrder.ASC)))) {
                throw new IllegalArgumentException("Exception with illegal argument: " + entry.getKey() + "=" + entry.getValue());
            }
        }
    }
}
>>>>>>> e80dcb2facb2aeb7dae916c7c7094cb4ceaafd8a
