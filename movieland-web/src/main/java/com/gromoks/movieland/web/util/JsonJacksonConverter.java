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

    public static <T,K>String toJson(K list, Class<T> clazz){
        try {
            return objectMapper.writerWithView(clazz).writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json convert based on view", e);
            throw new RuntimeException("Error in Json convert based on view",e);
        }
    }

    public static <T,K>String toJson(K list){
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("Error in Json converter", e);
            throw new RuntimeException("Error in Json convert",e);
        }
    }

    public static <K>String toJsonNormalMovie(K dtoMovies) {
       return toJson(dtoMovies, MovieViews.Normal.class);
    }

    public static <K>String toJsonExtendedMovie(K dtoMovies) {
        return toJson(dtoMovies, MovieViews.Extended.class);
    }

    public static <K>String toJsonFullMovie(K dtoMovies) {
        return toJson(dtoMovies, MovieViews.Full.class);
    }

    public static String toJsonGenre(List<Genre> genres) {
        return toJson(genres);
    }

}
