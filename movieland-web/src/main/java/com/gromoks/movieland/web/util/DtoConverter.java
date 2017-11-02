package com.gromoks.movieland.web.util;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.web.entity.MovieDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class DtoConverter {

    public static List<MovieDto> toMovieDtoList(List<Movie> movies) {
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (Movie movie : movies) {
            MovieDto movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setNameRussian(movie.getNameRussian());
            movieDto.setNameNative(movie.getNameNative());
            movieDto.setYearOfRelease(movie.getYearOfRelease());
            movieDto.setRating(movie.getRating());
            movieDto.setPrice(movie.getPrice());
            movieDto.setPicturePath(movie.getPicturePath());
            movieDto.setCountries(movie.getCountries());
            movieDto.setGenres(movie.getGenres());
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

}

