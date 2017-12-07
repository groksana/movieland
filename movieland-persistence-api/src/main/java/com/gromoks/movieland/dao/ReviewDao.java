package com.gromoks.movieland.dao;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;

public interface ReviewDao {
    Review addReview(Review review);
    void enrichSingleMovieByReview(Movie movie);
}
