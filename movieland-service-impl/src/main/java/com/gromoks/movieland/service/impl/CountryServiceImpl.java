package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.CountryDao;
import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CountryDao countryDao;

    @Override
    public void enrichSingleMovieByCountries(Movie movie) {
        log.info("Start to enrich single movie by countries");

        List<Country> countries = countryDao.getCountryListByMovie(movie);

        if (!Thread.currentThread().isInterrupted()) {
            movie.setCountries(countries);
            log.info("Finish to enrich single movie by countries");
        }
    }

    @Override
    public void enrichMoviesByCountries(List<Movie> movies) {
        log.info("Start to enrich movies by countries");

        List<MovieToCountry> movieToCountries = countryDao.getMovieToCountryList(movies);
        for (Movie movie : movies) {
            List<Country> countries = new ArrayList<>();
            for (MovieToCountry movieToCountry : movieToCountries) {
                if (movieToCountry.getMovieId() == movie.getId()) {
                    countries.add(new Country(movieToCountry.getCountryId(), movieToCountry.getCountry()));
                }
            }
            movie.setCountries(countries);
        }

        log.info("Finish to enrich movies by countries");
    }
}

