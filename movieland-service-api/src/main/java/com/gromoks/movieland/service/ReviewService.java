package com.gromoks.movieland.service;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;

public interface ReviewService {
    Review addReview(Review review);

    void enrichSingleMovieByReviewes(Movie movie);
}
