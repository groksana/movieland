package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;

public interface CurrencyService {
    Double getRateByName(String currencyName);
    void convertPriceInMovie(Movie movie, Double currencyRate);
}
