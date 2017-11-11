package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.service.GenreService;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/genre", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
public class GenreController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GenreService genreService;

    @RequestMapping
    public String getAll() {
        log.info("Sending request to get all genre");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = genreService.getAll();
        String json = JsonJacksonConverter.toJsonGenre(genres);
        log.info("Genres are received. It tooks {} ms", System.currentTimeMillis() - startTime);
        return json;
    }
}