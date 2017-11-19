package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;

public interface CurrencyService {
    void convertPriceInMovie(Movie movie, String currency);
}
