package com.gromoks.movieland.dao.entity;

public class MovieToCountry {
    private int movieId;
    private int countryId;
    private String country;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "MovieToCountry{" +
                "movieId=" + movieId +
                ", countryId=" + countryId +
                ", country='" + country + '\'' +
                '}';
    }
}
