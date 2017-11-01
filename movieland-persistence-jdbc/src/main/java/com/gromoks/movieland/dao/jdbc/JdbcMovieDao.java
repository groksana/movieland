package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.dao.entity.MovieToCountry;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.entity.Country;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcMovieDao implements MovieDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllMovieSQL;

    @Autowired
    private String getAllMovieToCountry;

    @Autowired
    private String getAllMovieToGenre;

    public List<Movie> getAll() {

        List<Movie> movies  = jdbcTemplate.query(getAllMovieSQL,
                new BeanPropertyRowMapper(Movie.class));
        List<MovieToCountry> movieToCountries = jdbcTemplate.query(getAllMovieToCountry,
                new BeanPropertyRowMapper<>(MovieToCountry.class));

        List<MovieToGenre> movieToGenres = jdbcTemplate.query(getAllMovieToGenre,
                new BeanPropertyRowMapper<>(MovieToGenre.class));

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            List<Country> countries = getCountryByMovieId(movieToCountries, movies.get(i).getId());
            movie.setCountries(countries);
            List<Genre> genres = getGenreByMovieId(movieToGenres, movies.get(i).getId());
            movie.setGenres(genres);
        }
        System.out.println(movies.size());
        return movies;
    }

    private List<Country> getCountryByMovieId(List<MovieToCountry> movie2Countries, int movieId) {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < movie2Countries.size(); i++) {
            if (movie2Countries.get(i).getMovieId() == movieId) {
                countries.add(new Country(movie2Countries.get(i).getCountryId(), movie2Countries.get(i).getCountry()));
            }
        }
        return countries;
    }

    private List<Genre> getGenreByMovieId(List<MovieToGenre> movie2Genres, int movieId) {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < movie2Genres.size(); i++) {
            if (movie2Genres.get(i).getMovieId() == movieId) {
                genres.add(new Genre(movie2Genres.get(i).getGenreId(), movie2Genres.get(i).getGenre()));
            }
        }
        return genres;
    }
}
