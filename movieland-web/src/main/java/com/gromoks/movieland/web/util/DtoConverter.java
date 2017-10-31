package com.gromoks.movieland.web.util;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.web.entity.MovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DtoConverter {

    public List<MovieDto> toMovieDtoList(List<Movie> movies) {
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            MovieDto movieDto = new MovieDto();
            movieDto.setId(movies.get(i).getId());
            movieDto.setNameRussian(movies.get(i).getNameRussian());
            movieDto.setNameNative(movies.get(i).getNameNative());
            movieDto.setYearOfRelease(movies.get(i).getYearOfRelease());
            movieDto.setRating(movies.get(i).getRating());
            movieDto.setPrice(movies.get(i).getPrice());
            movieDto.setPicturePath(movies.get(i).getPicturePath());
            movieDto.setCountries(movies.get(i).getCountries());
            movieDto.setGenres(movies.get(i).getGenres());
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

}

