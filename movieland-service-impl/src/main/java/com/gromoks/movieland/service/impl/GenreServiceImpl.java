package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.GenreDao;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{

    @Autowired
    private GenreDao genreDao;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }
}
