package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.CountryDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDao countryDao;

    @Override
    public void enrichSingleMovieByCountries(Movie movie) {
        countryDao.enrichSingleMovieByCountries(movie);
    }

    @Override
    public void enrichMoviesByCountries(List<Movie> movies) {
        countryDao.enrichMoviesByCountries(movies);
    }
}
