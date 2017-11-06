package com.gromoks.movieland.dao.cache;

import com.gromoks.movieland.entity.Genre;

import java.util.List;

public interface GenreCache {
    List<Genre> getAll();
    void cleanup();
    void fill();
}
