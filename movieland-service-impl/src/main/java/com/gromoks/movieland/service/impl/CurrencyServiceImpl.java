package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.CurrencyCache;
import com.gromoks.movieland.service.CurrencyService;
import com.gromoks.movieland.service.impl.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService{

    @Autowired
    private CurrencyCache currencyCache;

    public CurrencyServiceImpl() {

    }

    public CurrencyServiceImpl(CurrencyCache currencyCache) {
        this.currencyCache = currencyCache;
    }

    @Override
    public Double getRateByName(String currencyName) {
        List<CurrencyRate> currencyRates = currencyCache.getAll();
        Currency currency = Currency.getByName(currencyName);
        for (CurrencyRate currencyRate : currencyRates) {
            Currency cachedCurrency = Currency.getByName(currencyRate.getCurrency());
            if (currency == cachedCurrency) {
                return currencyRate.getRate();
            }
        }
        throw new IllegalArgumentException("Currency doesn't exists. Currency = " + currencyName);
    }

    public void convertPriceInMovie(Movie movie, Double currencyRate) {
        double convertedPrice = new BigDecimal(movie.getPrice()/currencyRate).setScale(2, RoundingMode.UP).doubleValue();
        movie.setPrice(convertedPrice);
    }
}
