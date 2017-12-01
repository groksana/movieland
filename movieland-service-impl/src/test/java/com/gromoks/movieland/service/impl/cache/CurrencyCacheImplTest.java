package com.gromoks.movieland.service.impl.cache;

import com.gromoks.movieland.entity.*;
import com.gromoks.movieland.service.entity.CurrencyRate;
import com.gromoks.movieland.service.cache.CurrencyCache;
import com.gromoks.movieland.service.CurrencyService;
import com.gromoks.movieland.service.impl.CurrencyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyCacheImplTest {
    @Mock
    private CurrencyCache mockCurrencyCache;

    @InjectMocks
    private CurrencyService currencyService = new CurrencyServiceImpl(mockCurrencyCache);

    private List<CurrencyRate> currencyRates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currencyRates = new ArrayList<>();
        currencyRates.add(new CurrencyRate("USD",26.0));
        currencyRates.add(new CurrencyRate("EUR",31.2));
    }

    @Test
    public void testConvertPriceInMovie() {
        when(mockCurrencyCache.getAll()).thenReturn(currencyRates);
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNameRussian("Тест");
        movie.setNameNative("Test");
        movie.setYearOfRelease(2016);
        movie.setDescription("Description for Test");
        movie.setRating(9.8);
        movie.setPrice(260);
        movie.setPicturePath("https");
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1,"США"));
        countries.add(new Country(2,"Украина"));
        movie.setCountries(countries);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1,"детектив"));
        genres.add(new Genre(2,"драма"));
        movie.setGenres(genres);
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1,new User(1,"Robin"),"Cool"));
        reviews.add(new Review(2,new User(2,"Kate"),"The best film"));
        movie.setReviews(reviews);

        currencyService.convertPriceInMovie(movie,"USD");
        assertEquals(movie.getPrice(),10.0,0);
    }
}
