package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;

import java.util.List;

public interface ReviewDao {
    Review addReview(Review review);

    List<Review> getReviewListByMovie(Movie movie);
}
