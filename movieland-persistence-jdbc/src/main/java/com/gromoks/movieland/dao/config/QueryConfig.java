package com.gromoks.movieland.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryConfig {

    @Bean
    public String getAllMovieSQL() {
        return "SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description,\n" +
                "                                       m.price, m.picturePath\n" +
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
    public String getMovieToReviewSQL() {
        return "SELECT r.id,r.movieId,r.userId,u.nickname,r.text\n" +
                " FROM review r, user u" +
                " WHERE r.userId = u.id AND r.movieId = ?;";
    }

    @Bean
    public String getRandomMovieSQL() {
        return " SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description," +
        " m.price, m.picturePath" +
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
        " m.price, m.picturePath" +
        " FROM movie m, movie2genre mg" +
        " WHERE m.id = mg.movieId and mg.genreId = ?";
    }

    @Bean
    public String getMoviesByIdSQL() {
        return " SELECT m.id, m.nameRussian, m.nameNative, m.yearOfRelease, m.description," +
                " m.price, m.picturePath" +
                " FROM movie m" +
                " WHERE m.id = ?";
    }

    @Bean
    public String getUserByEmailAndPasswordSQL() {
        return " SELECT id, nickname, email, role" +
                " FROM user " +
                " WHERE email = ? and password = ?";
    }

    @Bean
    public String addReviewSQL() {
        return "INSERT INTO REVIEW(movieId, userId, text) VALUES(:movieId, :userId, :text)";
    }

    @Bean
    public String addUserRatingSQL() {
        return "INSERT INTO RATING(movieId, userId, rating) VALUES(:movieId, :userId, :rating)";
    }

    @Bean
    public String getMovieRatingSQL() {
        return "SELECT movieId, ROUND(SUM(rating),1) rateSum, count(*) voteCount\n" +
                " FROM rating GROUP BY movieId;";
    }

    @Bean
    public String addMovieSQL() {
        return "INSERT INTO movie(nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath)" +
                " VALUES(:nameRussian, :nameNative, :yearOfRelease, :description, :rating, :price, :picturePath);";
    }

    @Bean
    public String addMovieToCountrySQL() {
        return "INSERT INTO movie2country(movieId, countryId)" +
                " VALUES(:movieId, :countryId);";
    }

    @Bean
    public String addMovieToGenreSQL() {
        return "INSERT INTO movie2genre(movieId, genreId)" +
                " VALUES(:movieId, :genreId);";
    }

    @Bean
    public String updateMovieSQL() {
        return "UPDATE movie SET nameRussian = :nameRussian, " +
                " nameNative = :nameNative, " +
                " picturePath = :picturePath " +
                "WHERE id = :id;";
    }

    @Bean
    public String deleteMovieToCountrySQL() {
        return "DELETE FROM movie2country WHERE movieId = :movieId;";
    }

    @Bean
    public String deleteMovieToGenreSQL() {
        return "DELETE FROM movie2genre WHERE movieId = :movieId;";
    }

    @Bean
    public String getAllReportMovieSQL() {
        return "SELECT " +
                "    m.id movieId," +
                "    m.nameRussian title," +
                "    m.description," +
                "    m.price," +
                "    GROUP_CONCAT(DISTINCT g.genre SEPARATOR ', ') genres," +
                "    ROUND(AVG(r.rating), 1) rating," +
                "    COUNT(rw.text) reviewCount" +
                " FROM" +
                "    movie m" +
                "        JOIN" +
                "    movie2genre mg ON m.id = mg.movieId" +
                "        JOIN" +
                "    genre g ON mg.genreId = g.id" +
                "        LEFT JOIN" +
                "    rating r ON m.id = r.movieId" +
                "        LEFT JOIN" +
                "    review rw ON m.id = rw.movieId" +
                " GROUP BY m.id , m.nameRussian , m.description , m.price";
    }

    @Bean
    public String insertReportInfoSQL() {
        return "INSERT INTO reportInfo(reportType, recipient, reportLink) " +
                "VALUES(:reportType, :recipient, :reportLink);";
    }

    @Bean
    public String getReportLinkByEmailSQL() {
        return "SELECT reportType, recipient, reportLink FROM reportInfo WHERE recipient = ? ";
    }
}

