package com.gromoks.movieland.service.impl.concurrent;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.service.CountryService;
import com.gromoks.movieland.service.GenreService;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.service.concurrent.ConcurrentEnrichmentMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ConcurrentEnrichmentMovieServiceImpl implements ConcurrentEnrichmentMovieService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @Autowired
    private CountryService countryService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ReviewService reviewService;

    @Value("${concurrent.enrichment.timeout}")
    private int timeout;

    @Override
    public void enrichMovie(Movie movie) {
        List<Future<?>> futureList = new ArrayList<>();

        Runnable refreshWithCountries = () -> {
            countryService.enrichSingleMovieByCountries(movie);
        };

        Runnable refreshWithGenres = () -> {
            genreService.enrichSingleMovieByGenres(movie);
        };

        Runnable refreshWithReviews = () -> {
            reviewService.enrichSingleMovieByReviewes(movie);
        };

        futureList.add(EXECUTOR_SERVICE.submit(refreshWithCountries));
        futureList.add(EXECUTOR_SERVICE.submit(refreshWithGenres));
        futureList.add(EXECUTOR_SERVICE.submit(refreshWithReviews));

        long remainTime = timeout;
        long elapsedTime;
        long startTime = System.currentTimeMillis();

        for (Future<?> future : futureList) {
            try {
                future.get(remainTime, TimeUnit.MILLISECONDS);
                elapsedTime = System.currentTimeMillis() - startTime;
                if (remainTime - elapsedTime <= 0) {
                    remainTime = 1;
                } else {
                    remainTime = remainTime - elapsedTime;
                }
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                log.info("Thread was interrupted");
                future.cancel(true);
            }
        }
    }
}