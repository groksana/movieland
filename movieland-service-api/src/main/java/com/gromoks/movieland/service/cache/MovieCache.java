package com.gromoks.movieland.service.cache;

import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Rating;

public interface MovieCache {
    void addUserMovieRating(Rating rating);
    Movie getById(int id);
    void enrichMovieWithRating(Movie movie);
}
