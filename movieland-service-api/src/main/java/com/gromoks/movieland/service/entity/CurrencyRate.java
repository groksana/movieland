package com.gromoks.movieland.service.entity;

public class CurrencyRate {
    private String currency;
    private Double rate;

    public CurrencyRate(String currency, Double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "CurrencyRate{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                '}';
    }
}
