package com.gromoks.movieland.dao.cache;

import com.gromoks.movieland.dao.GenreDao;
import com.gromoks.movieland.dao.entity.MovieToGenre;
import com.gromoks.movieland.entity.Genre;
import com.gromoks.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<Genre> getGenreListByMovie(Movie movie) {
        return genreDao.getGenreListByMovie(movie);
    }

    @Override
    public Map<Integer, List<Genre>> getMovieGenreLink(List<Movie> movies) {
        return genreDao.getMovieGenreLink(movies);
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString = "${cache.fixedRate.genre}", initialDelayString = "${cache.fixedRate.genre}")
    public void invalidate() {
        log.info("Start to fill genres to cache");
        cacheGenreList = genreDao.getAll();
        log.info("Finish to fill genres to cache. It obtains {} genres", cacheGenreList.size());
    }

}
