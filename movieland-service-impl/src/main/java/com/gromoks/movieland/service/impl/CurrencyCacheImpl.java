package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.service.CurrencyCache;
import com.gromoks.movieland.service.impl.util.JsonCurrencyConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyCacheImpl implements CurrencyCache {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile List<CurrencyRate> cacheCurrencyRateList;

    @Value("${web.url}")
    private String url;

    @Override
    public List<CurrencyRate> getAll() {
        List<CurrencyRate> copy = cacheCurrencyRateList;
        List<CurrencyRate> currencyRates = new ArrayList<>(copy);
        return currencyRates;
    }

    @Override
    @PostConstruct
    public void init() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        invalidate();
    }

    @Override
    @Scheduled(fixedRateString="${cache.fixedRate.currency}")
    public void invalidate() {
        log.info("Start to fill currency to cache");
        loadCurrency();
        log.info("Finish to fill currency to cache. It obtains {} currencies", cacheCurrencyRateList.size());
    }

    private void loadCurrency() {
        try {
            URL currencyUrl = new URL(url);
            URLConnection urlConnection = currencyUrl.openConnection();
            cacheCurrencyRateList = JsonCurrencyConverter.convertJsonToCurrency(urlConnection.getInputStream());
        } catch (IOException e) {
            log.warn("Currency loading by URL is fail. URL:" + url);
            throw new RuntimeException(e);
        }
    }
}
