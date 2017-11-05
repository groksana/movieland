package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }

    @Override
    public List<Movie> getRandom() {
        return movieDao.getRandom();
    }
}

