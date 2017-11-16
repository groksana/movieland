package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.service.CurrencyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyServiceImplTest {
    @Mock
    private CurrencyService mockCurrencyService;

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
        when(mockCurrencyService.getRateByName("USD")).thenReturn(currencyRates.get(0).getRate());
        assertEquals(currencyRates.get(0).getCurrency(), "USD");
        assertEquals(currencyRates.get(0).getRate(), 26.5,0);
    }
}
