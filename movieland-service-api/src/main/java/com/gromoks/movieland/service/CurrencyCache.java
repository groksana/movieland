package com.gromoks.movieland.service;

import com.gromoks.movieland.service.entity.CurrencyRate;

import java.util.List;

public interface CurrencyCache {
    List<CurrencyRate> getAll();
    void init();
    void invalidate();
}
