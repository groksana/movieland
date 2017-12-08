package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Movie;

import java.util.List;

public interface CountryDao {
    List<Country> getCountryListByMovie(Movie movie);

    List<MovieToCountry> getMovieToCountryList(List<Movie> movies);
}
