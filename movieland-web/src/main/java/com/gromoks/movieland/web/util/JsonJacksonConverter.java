package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MovieViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class JsonJacksonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(List<MovieDto> dtoMovies) throws JsonProcessingException {

        return objectMapper.writerWithView(MovieViews.Normal.class).writeValueAsString(dtoMovies);
    }

    public static String toExtendedJson(List<MovieDto> dtoMovies) throws JsonProcessingException {

        return objectMapper.writerWithView(MovieViews.Extended.class).writeValueAsString(dtoMovies);
    }

}
