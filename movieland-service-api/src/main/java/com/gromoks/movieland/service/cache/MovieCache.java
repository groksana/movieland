package com.gromoks.movieland.service.cache;

import com.gromoks.movieland.dao.entity.MovieRatingCache;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;

import java.util.List;

public interface MovieCache {
    void addUserMovieRating(Rating rating);
    void loadUserMovieRatingToDb();
    void fillMovieRatingCache();
    void enrichMovieWithRating(Movie movie);
}
