package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.cache.GenreCache;
import com.gromoks.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGenreDao implements GenreDao{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GenreRowMapper genreRowMapper = new GenreRowMapper();

    @Autowired
    private GenreCache genreCache;

    @Override
    public List<Genre> getAll() {
        log.info("Start query to get all genre from cache");
        long startTime = System.currentTimeMillis();
        List<Genre> genres  = genreCache.getAll();
        log.info("Finish query to get all genre from cache. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }
}
