package com.gromoks.movieland.dao.cache;

import com.gromoks.movieland.dao.jdbc.GenreDao;
import com.gromoks.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenericCacheImpl implements GenericCache<Genre>, GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<Genre> cacheGenreList;

    @Autowired
    private GenreDao genreDao;

    private boolean writeLock = false;

    public GenericCacheImpl() {
        cacheGenreList = new ArrayList<>();
    }

    public List<Genre> getAll() {
        if (writeLock) {
            synchronized (cacheGenreList) {
                return cacheGenreList;
            }
        } else {
            return cacheGenreList;
        }
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString="${cache.fixedRate}")
    public void invalidate() {
        writeLock = true;
        synchronized (cacheGenreList) {
            cleanup();
            fill();
        }
        writeLock = false;
    }

    private void cleanup() {
            log.info("Start cleanup of cached genres");
            cacheGenreList.clear();
            log.info("Finish cleanup of cached genres");
    }

    private void fill() {
        log.info("Start to fill genres to cache");
        cacheGenreList = genreDao.getAll();
        log.info("Finish to fill genres to cache. It obtains {} genres", cacheGenreList.size());
    }
}
