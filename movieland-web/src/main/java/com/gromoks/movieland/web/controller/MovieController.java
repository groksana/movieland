package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.CurrencyService;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.service.entity.UserRole;
import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.web.entity.*;
import com.gromoks.movieland.web.security.Protected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gromoks.movieland.web.util.DtoConverter.*;
import static com.gromoks.movieland.web.util.JsonJacksonConverter.*;

@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MovieService movieService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(@RequestParam(required = false) LinkedHashMap<String, String> requestParamMap) {
        log.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        validateMovieRequest(requestParamMap);
        List<Movie> movies = movieService.getAll(requestParamMap);
        List<MovieDto> dtoMovies = toMovieDtoList(movies);
        String json = toJsonNormalMovie(dtoMovies);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public String getRandom() {
        log.info("Sending request to get 3 random movies");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = movieService.getRandom();
        List<MovieDto> dtoMovies = toMovieDtoList(movies);
        String json = toJsonExtendedMovie(dtoMovies);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/genre/{genreId}", method = RequestMethod.GET)
    public String getByGenreId(@PathVariable int genreId, @RequestParam(required = false) LinkedHashMap<String, String> requestParamMap) {
        log.info("Sending request to get movies by genre with id = {}", genreId);
        long startTime = System.currentTimeMillis();

        validateMovieRequest(requestParamMap);
        List<Movie> movies = movieService.getByGenreId(genreId, requestParamMap);
        List<MovieDto> dtoMovies = toMovieDtoList(movies);
        String json = toJsonNormalMovie(dtoMovies);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/{movieId}", method = RequestMethod.GET)
    public String getById(@PathVariable int movieId, @RequestParam(value = "currency", defaultValue = "UAH") String currency) {
        log.info("Sending request to get movies by id = {} with currency = {}", movieId, currency);
        long startTime = System.currentTimeMillis();

        Movie movie = movieService.getById(movieId);
        currencyService.convertPriceInMovie(movie, currency);
        MovieDto dtoMovie = toMovieDto(movie);
        String json = toJsonFullMovie(dtoMovie);

        log.info("Movies are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }

    @RequestMapping(value = "/{movieId}/rate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Protected(UserRole.USER)
    public ResponseEntity<?> addMovieRating(@PathVariable int movieId, @RequestBody Rating rating) {
        log.info("Sending request to add movie rate");
        long startTime = System.currentTimeMillis();

        User user = authenticationService.getAuthenticatedUser();

        rating.setMovieId(movieId);
        rating.setUserId(user.getId());
        movieService.addMovieRating(rating);

        log.info("Rating has been added. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Protected(UserRole.ADMIN)
    public ResponseEntity<?> add(@RequestBody MoviePostDto moviePostDto) {
        log.info("Sending request to add movie");
        long startTime = System.currentTimeMillis();

        Movie movie = parseMoviePostDto(moviePostDto);
        movieService.add(movie);

        log.info("Movie has been added. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Protected(UserRole.ADMIN)
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody MoviePostDto moviePostDto) {
        log.info("Sending request to update movie");
        long startTime = System.currentTimeMillis();

        Movie movie = parseMoviePostDto(moviePostDto);
        movie.setId(id);
        movieService.edit(movie);

        log.info("Movie has been updated. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<Object>(HttpStatus.OK);
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
