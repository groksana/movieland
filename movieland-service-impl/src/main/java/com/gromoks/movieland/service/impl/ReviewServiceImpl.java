package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.ReviewDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.service.cache.MovieCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private MovieCache movieCache;

    @Override
    public Review addReview(Review review) {
        reviewDao.addReview(review);
        movieCache.removeById(review.getMovieId());
        return review;
    }

    @Override
    public void enrichSingleMovieByReviewes(Movie movie) {
        log.info("Start to enrich single movie by reviews");

        List<Review> reviews = reviewDao.getReviewListByMovie(movie);
        if (!Thread.currentThread().isInterrupted()) {
            movie.setReviews(reviews);
            log.info("Finish to enrich single movie by reviews");
        }
    }
}
