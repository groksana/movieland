package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.ReviewDao;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.service.cache.MovieCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

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
        reviewDao.enrichSingleMovieByReview(movie);
    }
}
