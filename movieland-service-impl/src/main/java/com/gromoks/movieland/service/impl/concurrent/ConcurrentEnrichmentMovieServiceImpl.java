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

    private final ExecutorService ENRICH_EXECUTOR_SERVICE = Executors.newCachedThreadPool();

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
        long remainTime = timeout;
        long elapsedTime;
        long startTime = System.currentTimeMillis();

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

        futureList.add(ENRICH_EXECUTOR_SERVICE.submit(refreshWithCountries));
        futureList.add(ENRICH_EXECUTOR_SERVICE.submit(refreshWithGenres));
        futureList.add(ENRICH_EXECUTOR_SERVICE.submit(refreshWithReviews));

        for (Future<?> future : futureList) {
            try {
                future.get(remainTime, TimeUnit.MILLISECONDS);
                elapsedTime = System.currentTimeMillis() - startTime;
                if (remainTime - elapsedTime < 0) {
                    remainTime = 0;
                } else {
                    remainTime = remainTime - elapsedTime;
                }
            } catch (TimeoutException e) {
                log.warn("One of parallel threads was interrupted due timeout");
                future.cancel(true);
            } catch (InterruptedException e) {
                log.error("The main thread was interrupted");
                for (Future<?> futureResult : futureList) {
                    futureResult.cancel(true);
                }
                throw new RuntimeException("The main thread was interrupted");
            } catch (ExecutionException e) {
                log.warn("The result of movie enrichment can't be received");
            }
        }
    }
}