package com.gromoks.movieland.service.cache;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;

public interface MovieCache {
    void addUserMovieRating(Rating rating);
    void enrichMovieWithRating(Movie movie);
}
