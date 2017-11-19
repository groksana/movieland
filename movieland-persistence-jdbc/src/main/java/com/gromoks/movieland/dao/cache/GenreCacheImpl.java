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
public class GenreCacheImpl implements GenericCache<Genre>, GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile List<Genre> cacheGenreList;

    @Autowired
    private GenreDao genreDao;

    public List<Genre> getAll() {
        List<Genre> copy = cacheGenreList;
        return new ArrayList<>(copy);
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString="${cache.fixedRate.genre}")
    public void invalidate() {
        log.info("Start to fill genres to cache");
        cacheGenreList = genreDao.getAll();
        log.info("Finish to fill genres to cache. It obtains {} genres", cacheGenreList.size());
    }

}
