package com.gromoks.movieland.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryConfig {

    @Bean
    public String getAllMovieSQL() {
        return "SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description,\n" +
                "                                       m.rating, m.price, m.picturePath\n" +
                "        FROM movie m";
    }

    @Bean
    public String getAllMovieToCountrySQL() {
        return "SELECT mc.movieId, c.id, c.country" +
        " FROM country c, movie2country mc" +
        " WHERE c.id = mc.countryId and mc.movieId in (:ids);";
    }

    @Bean
    public String getAllMovieToGenreSQL() {
        return " SELECT mg.movieId, g.id, g.genre " +
        " FROM genre g, movie2genre mg" +
        " WHERE g.id = mg.genreId and mg.movieId in (:ids);";
    }

    @Bean
    public String getAllMovieToReviewSQL() {
        return "SELECT r.id,r.movieId,r.userId,u.nickname,r.text\n" +
                " FROM review r, user u" +
                " WHERE r.userId = u.id AND r.movieId in (:ids);";
    }

    @Bean
    public String getRandomMovieSQL() {
        return " SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description," +
        " m.rating, m.price, m.picturePath" +
        " FROM movie m" +
        " ORDER BY RAND() LIMIT 3;";
    }

    @Bean
    public String getAllGenreSQL() {
        return " SELECT g.id, g.genre" +
        " FROM genre g;";
    }

    @Bean
    public String getMoviesByGenreIdSQL() {
        return " SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description," +
        " m.rating, m.price, m.picturePath" +
        " FROM movie m, movie2genre mg" +
        " WHERE m.id = mg.movieId and mg.genreId = ?";
    }

    @Bean
    public String getMoviesByIdSQL() {
        return " SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description," +
                " m.rating, m.price, m.picturePath" +
                " FROM movie m" +
                " WHERE m.id = ?";
    }
}

