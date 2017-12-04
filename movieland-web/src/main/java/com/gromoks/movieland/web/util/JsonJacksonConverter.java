package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.web.entity.MoviePostDto;
import com.gromoks.movieland.web.entity.MovieViews;
import com.gromoks.movieland.web.entity.UserTokenViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JsonJacksonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    final static Logger log = LoggerFactory.getLogger(JsonJacksonConverter.class);

    public static <K> String toJsonNormalMovie(K dtoMovies) {
        return toJson(dtoMovies, MovieViews.Normal.class);
    }

    public static <K> String toJsonExtendedMovie(K dtoMovies) {
        return toJson(dtoMovies, MovieViews.Extended.class);
    }

    public static <K> String toJsonFullMovie(K dtoMovies) {
        return toJson(dtoMovies, MovieViews.Full.class);
    }

    public static String toJsonGenre(List<Genre> genres) {
        return toJson(genres);
    }

    public static <K> String toJsonNormalUserToken(K dtoUserToken) {
        return toJson(dtoUserToken, UserTokenViews.Normal.class);
    }

    public static String toJsonReview(Review review) {
        return toJson(review);
    }

    public static Review parseReview(String json) {
        log.info("Start parsing review from json {}", json);
        long startTime = System.currentTimeMillis();

        Review review = parseValue(json, Review.class);

        long time = System.currentTimeMillis() - startTime;
        log.info("Review {} is received. It tooks {} ms", review, time);

        return review;
    }

    private static <T> T parseValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T, K> String toJson(K list, Class<T> clazz) {
        try {
            return objectMapper.writerWithView(clazz).writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json convert based on view", e);
            throw new RuntimeException("Error in Json convert based on view", e);
        }
    }

    private static <K> String toJson(K list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json converter", e);
            throw new RuntimeException("Error in Json convert", e);
        }
    }
}
