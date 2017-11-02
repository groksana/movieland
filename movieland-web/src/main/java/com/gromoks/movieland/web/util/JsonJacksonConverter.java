package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MovieViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonJacksonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    final static Logger log = LoggerFactory.getLogger(JsonJacksonConverter.class);

    public static String toJson(List<MovieDto> dtoMovies) {

        try {
            return objectMapper.writerWithView(MovieViews.Normal.class).writeValueAsString(dtoMovies);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return null;
    }

    public static String toExtendedJson(List<MovieDto> dtoMovies) {

        try {
            return objectMapper.writerWithView(MovieViews.Extended.class).writeValueAsString(dtoMovies);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return null;
    }

}
