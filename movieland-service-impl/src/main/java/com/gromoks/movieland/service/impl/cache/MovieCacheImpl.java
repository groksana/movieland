package com.gromoks.movieland.service.impl.cache;

import com.google.common.collect.MapMaker;
import com.gromoks.movieland.dao.entity.CachedMovieRating;
import com.gromoks.movieland.dao.MovieDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;
import com.gromoks.movieland.service.CountryService;
import com.gromoks.movieland.service.GenreService;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.service.cache.MovieCache;
import com.gromoks.movieland.service.concurrent.ConcurrentEnrichmentMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Service
public class MovieCacheImpl implements MovieCache {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Integer, CachedMovieRating> cachedMovieRatingMap = new ConcurrentHashMap<>();

    private volatile ConcurrentLinkedQueue<Rating> cachedUserRatingQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentMap<Integer, Movie> cachedMovie = new MapMaker().softValues().makeMap();

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private ConcurrentEnrichmentMovieService concurrentEnrichmentMovieService;

    @Override
    public void addUserMovieRating(Rating rating) {
        log.info("Start to add user rating for movie {} to cache", rating.getMovieId());

        cachedUserRatingQueue.add(rating);
        CachedMovieRating cachedMovieRating = cachedMovieRatingMap.computeIfAbsent(rating.getMovieId(),
                k -> {
                    return new CachedMovieRating(rating.getMovieId(), 0, 0);
                });

        log.debug("Start to update rating for movie {}", rating.getMovieId());
        cachedMovieRating.getMovieId().updateAndGet(k -> {
            cachedMovieRating.getVoteCount().incrementAndGet();
            double newRateSum = Double.longBitsToDouble(cachedMovieRating.getRateSum().longValue()) + rating.getRating();
            cachedMovieRating.getRateSum().getAndSet(Double.doubleToLongBits(newRateSum));
            return rating.getMovieId();
        });
        log.debug("Get updated rateSum = {}", Double.longBitsToDouble(cachedMovieRating.getRateSum().longValue()));
        log.debug("Get updated voteCount = {}", cachedMovieRating.getVoteCount());

        log.info("Finish to add user rating for movie {} to cache", rating.getMovieId());
    }

    @Override
    public Movie getById(int id) {
        log.debug("Start get movie by id from cache. Id = {}", id);
        Movie movie = new Movie(cachedMovie.computeIfAbsent(id, movieId -> {
            log.debug("Add to cache if absent");
            Movie refreshedMovie = movieDao.getById(movieId);
            concurrentEnrichmentMovieService.enrichMovie(refreshedMovie);
            return refreshedMovie;
        }));
        log.debug("Finish get movie by id from cache");
        return movie;
    }

    @Override
    public void removeById(int movieId) {
        log.debug("Remove movie from cache");
        cachedMovie.remove(movieId);
        log.debug("Finish to remove movie from cache");
    }

    @Scheduled(fixedRateString = "${cache.fixedRate.rating}", initialDelayString = "${cache.fixedRate.rating}")
    private void loadUserMovieRatingToDb() {
        ConcurrentLinkedQueue<Rating> copyCachedUserRatingQueue = cachedUserRatingQueue;
        cachedUserRatingQueue = new ConcurrentLinkedQueue<>();
        movieDao.addMovieRatings(copyCachedUserRatingQueue);
    }

    @PostConstruct
    private void fillMovieRatingCache() {
        log.info("Start to fill cache with movie rating");
        List<CachedMovieRating> cachedMovieRatingCaches = movieDao.getMovieRating();

        for (CachedMovieRating cachedMovieRating : cachedMovieRatingCaches) {
            cachedMovieRatingMap.put(cachedMovieRating.getMovieId().intValue(), cachedMovieRating);
        }

        log.info("Finish to fill cache with movie rating");
    }

    @Override
    public void enrichMovieWithRating(Movie movie) {
        log.info("Start to enrich movie by rating with Id {}", movie.getId());
        CachedMovieRating cachedMovieRating = cachedMovieRatingMap.get(movie.getId());

        if (cachedMovieRating != null) {
            double rating = new BigDecimal(Double.longBitsToDouble(cachedMovieRating.getRateSum().longValue()) / cachedMovieRating.getVoteCount().intValue()).setScale(1, RoundingMode.UP).doubleValue();
            movie.setRating(rating);
            log.info("Movie with Id {} has been enriched by rating", movie.getId());
        } else {
            log.info("Rating for movie {} is absent", movie.getId());
        }
    }
}
