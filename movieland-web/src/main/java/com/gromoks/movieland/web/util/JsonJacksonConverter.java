package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MovieViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonJacksonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    final static Logger log = LoggerFactory.getLogger(JsonJacksonConverter.class);

    public static <T>String toJson(List list, Class<T> clazz){
        try {
            return objectMapper.writerWithView(clazz).writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json convert based on view", e);
            throw new RuntimeException(e);
        }
    }

    public static <T>String toJson(List list){
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json converter", e);
            throw new RuntimeException(e);
        }
    }

    public static String toJsonNormalMovie(List<MovieDto> dtoMovies) {
       return toJson(dtoMovies, MovieViews.Normal.class);
    }

    public static String toJsonExtendedMovie(List<MovieDto> dtoMovies) {
        return toJson(dtoMovies, MovieViews.Extended.class);
    }

    public static String toJsonGenre(List<Genre> genres) {
        return toJson(genres);
    }

}
