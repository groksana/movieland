package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.CurrencyRate;

import java.util.List;

public interface CurrencyService {
    Double getRateByName(String currencyName);
    void init();
    void invalidate();
}
