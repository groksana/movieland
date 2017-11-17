package com.gromoks.movieland.service.impl.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.CurrencyRate;
import com.gromoks.movieland.service.impl.entity.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonCurrencyConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger log = LoggerFactory.getLogger(JsonCurrencyConverter.class);

    public static List<CurrencyRate> convertJsonToCurrency(InputStream inputStream) {
        List<CurrencyRate> currencies = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(inputStream);
            String currencyJson;

            for(JsonNode node : root) {
                currencyJson = node.get("cc").asText();
                if (currencyJson.equalsIgnoreCase(String.valueOf(Currency.EUR))
                        || currencyJson.equalsIgnoreCase(String.valueOf(Currency.USD))) {
                    currencies.add(new CurrencyRate(currencyJson,node.get("rate").asDouble()));
                }
            }
            return currencies;
        } catch (IOException e) {
            log.warn("Issue to get json input stream");
            throw new RuntimeException(e);
        }
    }
}
