package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();
}
