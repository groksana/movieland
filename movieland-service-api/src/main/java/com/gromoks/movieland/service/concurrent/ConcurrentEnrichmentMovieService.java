package com.gromoks.movieland.service.concurrent;

import com.gromoks.movieland.entity.Movie;

public interface ConcurrentEnrichmentMovieService {
    void enrichMovie(Movie movie);
}
