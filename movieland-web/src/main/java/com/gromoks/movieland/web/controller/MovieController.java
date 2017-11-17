package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.CurrencyService;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.web.entity.*;
import com.gromoks.movieland.web.util.DtoConverter;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieService movieService;

    @Autowired
    private CurrencyService currencyService;

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

    @RequestMapping(value = "/{movieId}")
    public String getById(@PathVariable int movieId, @RequestParam(value = "currency", defaultValue = "UAH") String currency) {
        log.info("Sending request to get movies by id = {}", movieId);
        long startTime = System.currentTimeMillis();
        Movie movie = movieService.getById(movieId);
        if (!currency.equalsIgnoreCase("UAH")) {
            Double currencyRate = currencyService.getRateByName(currency);
            currencyService.convertPriceInMovie(movie, currencyRate);
        }
        MovieDto dtoMovie = DtoConverter.toMovieDto(movie);
        String json = JsonJacksonConverter.toJsonFullMovie(dtoMovie);
        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    private void validateMovieRequest(LinkedHashMap<String, String> requestParamMap) {
        for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
            RequestParameter requestParameter = RequestParameter.getByName(entry.getKey());
            SortingOrder sortingOrder = SortingOrder.getByName(entry.getValue());

            if (!(requestParameter == RequestParameter.RATING && sortingOrder == SortingOrder.DESC)
                && !(requestParameter == RequestParameter.PRICE && sortingOrder != null)) {
                throw new IllegalArgumentException("Exception with illegal argument: " + requestParameter + "=" + sortingOrder);
            }
        }
    }
}
