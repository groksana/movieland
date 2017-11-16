package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.service.CurrencyService;
import com.gromoks.movieland.service.impl.entity.Currency;
import com.gromoks.movieland.service.impl.util.JsonJacksonConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile List<CurrencyRate> cacheCurrencyRateList;

    @Value("${web.url}")
    private String url;

    @Override
    @PostConstruct
    public void init() {
        log.info("Start to fill currency to cache");
        loadCurrency();
        log.info("Finish to fill currency to cache. It obtains {} currencies", cacheCurrencyRateList.size());
    }

    @Override
    @Scheduled(fixedRateString="${cache.fixedRate.currency}")
    public void invalidate() {
        log.info("Start to fill currency to cache");
        loadCurrency();
        log.info("Finish to fill currency to cache. It obtains {} currencies", cacheCurrencyRateList.size());
    }

    @Override
    public Double getRateByName(String currencyName) {
        List<CurrencyRate> copy = cacheCurrencyRateList;
        Currency currency = Currency.getByName(currencyName);
        Double rate = null;
        for(CurrencyRate currencyRate : copy) {
            Currency cachedCurrency = Currency.getByName(currencyRate.getCurrency());
            if (currency == cachedCurrency) {
                rate = currencyRate.getRate();
            }
        }
        return rate;
    }

    private void loadCurrency() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        try {
            URL currencyUrl = new URL(url);
            URLConnection urlConnection = currencyUrl.openConnection();
            cacheCurrencyRateList = JsonJacksonConverter.convertJsonToCurrency(urlConnection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
