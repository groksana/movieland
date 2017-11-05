package com.gromoks.movieland.dao.cache;

import com.gromoks.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.gromoks.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GenreCacheImpl implements GenreCache {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<Genre> cacheGenreList;

    private final GenreRowMapper genreRowMapper = new GenreRowMapper();

    private final long timerInterval = 14400;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllGenreSQL;

    public GenreCacheImpl() {
        cacheGenreList = new ArrayList<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                cleanup();
                fill();
            }
        }, timerInterval, timerInterval, TimeUnit.SECONDS);
    }

    public List<Genre> getAll() {
        synchronized (cacheGenreList) {
            log.info("Start to get all cached genres");
            if (cacheGenreList.size() == 0) {
                fill();
            }
            log.info("Finish to get all cached genres");
            return cacheGenreList;
        }
    }

    public void cleanup() {
        synchronized (cacheGenreList) {
            log.info("Start cleanup of cached genres");
            cacheGenreList.clear();
            log.info("Finish cleanup of cached genres");
        }
    }

    public void fill() {
        synchronized (cacheGenreList) {
        log.info("Start to fill genres to cache");
        cacheGenreList = jdbcTemplate.query(getAllGenreSQL, genreRowMapper);
        log.info("Finish to fill genres to cache");
        }
    }
}
