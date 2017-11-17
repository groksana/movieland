package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.service.CurrencyCache;
import com.gromoks.movieland.service.CurrencyService;
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
        currencyRates.add(new CurrencyRate("USD",26.5));
        currencyRates.add(new CurrencyRate("EUR",31.2));
    }

    @Test
    public void testGetRateByNames() {
        when(mockCurrencyCache.getAll()).thenReturn(currencyRates);
        assertEquals(currencyService.getRateByName(currencyRates.get(0).getCurrency()),26.5,0);
        assertEquals(currencyService.getRateByName(currencyRates.get(1).getCurrency()),31.2,0);
    }
}
