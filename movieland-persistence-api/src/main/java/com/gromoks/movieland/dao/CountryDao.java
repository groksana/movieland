package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Movie;

import java.util.List;
import java.util.Map;

public interface CountryDao {
    List<Country> getCountryListByMovie(Movie movie);

    Map<Integer, List<Country>> getMovieCountryLink(List<Movie> movies);
}
