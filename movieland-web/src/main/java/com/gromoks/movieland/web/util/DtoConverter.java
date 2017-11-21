package com.gromoks.movieland.web.util;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.UserTokenDto;

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
            movieDto.setDescription(movie.getDescription());
            movieDto.setRating(movie.getRating());
            movieDto.setPrice(movie.getPrice());
            movieDto.setPicturePath(movie.getPicturePath());
            movieDto.setCountries(movie.getCountries());
            movieDto.setGenres(movie.getGenres());
            movieDto.setReviews(movie.getReviews());
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

    public static MovieDto toMovieDto(Movie movie) {
            MovieDto movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setNameRussian(movie.getNameRussian());
            movieDto.setNameNative(movie.getNameNative());
            movieDto.setYearOfRelease(movie.getYearOfRelease());
            movieDto.setDescription(movie.getDescription());
            movieDto.setRating(movie.getRating());
            movieDto.setPrice(movie.getPrice());
            movieDto.setPicturePath(movie.getPicturePath());
            movieDto.setCountries(movie.getCountries());
            movieDto.setGenres(movie.getGenres());
            movieDto.setReviews(movie.getReviews());
        return movieDto;
    }

    public static UserTokenDto toUserTokenDto(UserToken userToken) {
        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setUuid(userToken.getUuid());
        userTokenDto.setNickname(userToken.getNickname());
        userTokenDto.setEmail(userToken.getEmail());
        userTokenDto.setInitTimeInMs(userToken.getInitTimeInMs());
        return userTokenDto;
    }

}

