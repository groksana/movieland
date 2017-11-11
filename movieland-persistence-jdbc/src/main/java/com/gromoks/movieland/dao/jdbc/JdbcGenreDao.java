package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.jdbc.mapper.GenreRowMapper;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllGenreSQL;

    @Override
    public List<Genre> getAll() {
        log.info("Start query to get all genre from db");
        long startTime = System.currentTimeMillis();
        List<Genre> genres  = jdbcTemplate.query(getAllGenreSQL, genreRowMapper);
        log.info("Finish query to get all genre from db. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }
}
