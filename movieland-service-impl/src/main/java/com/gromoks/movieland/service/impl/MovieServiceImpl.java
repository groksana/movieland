package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Override
    public List<Movie> getAll(LinkedHashMap<String,String> requestParamMap) {
        return movieDao.getAll(requestParamMap);
    }

    @Override
    public List<Movie> getRandom() {
        return movieDao.getRandom();
    }

    @Override
    public List<Movie> getByGenreId(int id, LinkedHashMap<String,String> requestParamMap) {
        return movieDao.getByGenreId(id, requestParamMap);
    }

    @Override
    public Movie getById(int id) {
        return movieDao.getById(id);
    }
}

