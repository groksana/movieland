package com.gromoks.movieland.web.util;

import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MoviePostDto;
import com.gromoks.movieland.web.entity.UserTokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DtoConverter {

    final static Logger log = LoggerFactory.getLogger(DtoConverter.class);

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
        userTokenDto.setNickname(userToken.getUser().getNickname());
        userTokenDto.setEmail(userToken.getUser().getEmail());
        userTokenDto.setExpireDateTime(userToken.getExpireDateTime());
        return userTokenDto;
    }

    public static Movie parseMoviePostDto(MoviePostDto moviePostDto) {
        Movie movie = new Movie();
        List<Country> countries = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        movie.setNameRussian(moviePostDto.getNameRussian());
        movie.setNameNative(moviePostDto.getNameNative());
        movie.setYearOfRelease(moviePostDto.getYearOfRelease());
        movie.setDescription(moviePostDto.getDescription());
        movie.setRating(moviePostDto.getRating());
        movie.setPrice(moviePostDto.getPrice());
        movie.setPicturePath(moviePostDto.getPicturePath());

        Integer[] dtoCountries = moviePostDto.getCountries();

        for (Integer countryId : dtoCountries) {
            Country country = new Country();
            country.setId(countryId);
            countries.add(country);
        }
        movie.setCountries(countries);

        Integer[] dtoGenres = moviePostDto.getGenres();

        for (Integer genreId : dtoGenres) {
            Genre genre = new Genre(genreId);
            genres.add(genre);
        }
        movie.setGenres(genres);

        return movie;
    }

}

