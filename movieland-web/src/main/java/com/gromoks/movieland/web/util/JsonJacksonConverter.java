package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MovieViews;

import java.util.List;

public class JsonJacksonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(List<MovieDto> dtoMovies) {

        try {
            return objectMapper.writerWithView(MovieViews.Normal.class).writeValueAsString(dtoMovies);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toExtendedJson(List<MovieDto> dtoMovies) {

        try {
            return objectMapper.writerWithView(MovieViews.Extended.class).writeValueAsString(dtoMovies);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
