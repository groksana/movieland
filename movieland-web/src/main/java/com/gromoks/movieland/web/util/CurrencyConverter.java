package com.gromoks.movieland.web.util;

import com.gromoks.movieland.entity.Movie;

import java.math.BigDecimal;

public class CurrencyConverter {
    public static void convertMoviePrice(Movie movie, Double currencyRate) {
        Double convertedPrice = movie.getPrice()/currencyRate;
        movie.setPrice(convertedPrice);
    }
}
