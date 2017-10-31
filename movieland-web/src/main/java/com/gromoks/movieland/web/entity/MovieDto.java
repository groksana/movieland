package com.gromoks.movieland.web.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.gromoks.movieland.entity.*;

import java.util.List;

public class MovieDto {

    @JsonView(MovieViews.Normal.class)
    private int id;

    @JsonView(MovieViews.Normal.class)
    private String nameRussian;

    @JsonView(MovieViews.Normal.class)
    private String nameNative;

    @JsonView(MovieViews.Normal.class)
    private int yearOfRelease;

    @JsonView(MovieViews.Normal.class)
    private double rating;

    @JsonView(MovieViews.Normal.class)
    private double price;

    @JsonView(MovieViews.Normal.class)
    private String picturePath;

    @JsonView(MovieViews.Extended.class)
    private List<Country> countries;

    @JsonView(MovieViews.Extended.class)
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRussian() {
        return nameRussian;
    }

    public void setNameRussian(String nameRussian) {
        this.nameRussian = nameRussian;
    }

    public String getNameNative() {
        return nameNative;
    }

    public void setNameNative(String nameNative) {
        this.nameNative = nameNative;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void movieToMovieDtoTransform(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.id = movie.getId();
        movieDto.nameRussian = movie.getNameRussian();
        movieDto.nameNative = movie.getNameNative();
        movieDto.yearOfRelease = movie.getYearOfRelease();
        movieDto.rating = movie.getRating();
        movieDto.price = movie.getPrice();
        movieDto.picturePath = movie.getPicturePath();
        movieDto.countries = movie.getCountries();
        movieDto.genres = movieDto.getGenres();
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", nameRussian='" + nameRussian + '\'' +
                ", nameNative='" + nameNative + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", rating=" + rating +
                ", price=" + price +
                ", picturePath='" + picturePath + '\'' +
                ", countries=" + countries +
                ", genres=" + genres +
                '}';
    }
}
