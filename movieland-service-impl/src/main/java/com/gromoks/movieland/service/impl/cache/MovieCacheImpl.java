package com.gromoks.movieland.service.impl.cache;

import com.gromoks.movieland.dao.entity.MovieRatingCache;
import com.gromoks.movieland.dao.jdbc.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.service.cache.MovieCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class MovieCacheImpl implements MovieCache {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private final List<Rating> cacheUserMovieRatingList = new ArrayList<>();

    private final Map<Integer, MovieRatingCache> cacheMovieRatingMap = new ConcurrentHashMap<>();

    @Autowired
    private MovieDao movieDao;

    @Override
    public void addUserMovieRating(Rating rating) {
        log.info("Start to add user rating for movie {} to cache", rating.getMovieId());
        lock.writeLock().lock();

        try {
            cacheUserMovieRatingList.add(rating);
            MovieRatingCache movieRatingCache = cacheMovieRatingMap.get(rating.getMovieId());

            if (movieRatingCache != null) {
                log.debug("Start to update rating for movie {}", rating.getMovieId());

                int newVoteCount = movieRatingCache.getVoteCount() + 1;
                double newRateSum = movieRatingCache.getRateSum() + rating.getRating();

                movieRatingCache.setVoteCount(newVoteCount);
                movieRatingCache.setRateSum(newRateSum);

                log.debug("Finish to update movie rating value with newVoteCount = {} and newRateSum = {}", newVoteCount, newRateSum);
            } else {
                log.debug("Start to insert rating for movie {}", rating.getMovieId());

                movieRatingCache = new MovieRatingCache();
                movieRatingCache.setMovieId(rating.getMovieId());
                movieRatingCache.setVoteCount(1);
                movieRatingCache.setRateSum(rating.getRating());

                log.debug("Finish to insert movie rating");
            }

            cacheMovieRatingMap.put(rating.getMovieId(), movieRatingCache);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @Scheduled(fixedRateString = "${cache.fixedRate.rating}", initialDelayString = "${cache.fixedRate.rating}")
    public void loadUserMovieRatingToDb() {
        lock.writeLock().lock();

        try {
            movieDao.addMovieRating(cacheUserMovieRatingList);
            cacheUserMovieRatingList.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @PostConstruct
    public void fillMovieRatingCache() {
        log.info("Start to fill cache with movie rating");
        List<MovieRatingCache> movieRatingCaches = movieDao.getMovieRating();

        for (MovieRatingCache movieRatingCache : movieRatingCaches) {
            cacheMovieRatingMap.put(movieRatingCache.getMovieId(), movieRatingCache);
        }

        log.info("Finish to fill cache with movie rating");
    }

    @Override
    public void enrichMovieWithRating(Movie movie) {
        log.info("Start to enrich movie with Id {}", movie.getId());
        MovieRatingCache movieRatingCache = cacheMovieRatingMap.get(movie.getId());

        if (movieRatingCache != null) {
            double rating = new BigDecimal(movieRatingCache.getRateSum() / movieRatingCache.getVoteCount()).setScale(2, RoundingMode.UP).doubleValue();
            movie.setRating(rating);
            log.info("Movie with Id {} has been enriched", movie.getId());
        } else {
            log.info("Rating for movie {} is absent", movie.getId());
        }
    }
}
