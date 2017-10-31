package com.gromoks.movieland.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.web.entity.MovieDto;
import com.gromoks.movieland.web.entity.MovieViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonJacksonConverter {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DtoConverter dtoConverter;

    public String toJson(List<Movie> movies) throws JsonProcessingException {

        String json = null;
        List<MovieDto> movieDtos = new ArrayList<MovieDto>();
        //DtoConverter dtoConverter = new DtoConverter();
        movieDtos = dtoConverter.toMovieDtoList(movies);

        json = objectMapper.writerWithView(MovieViews.Normal.class).writeValueAsString(movieDtos);
        //json = objectMapper.writeValueAsString(movieDtos);

        return json;
    }
}
