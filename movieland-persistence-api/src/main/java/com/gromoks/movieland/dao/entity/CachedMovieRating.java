package com.gromoks.movieland.dao.entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CachedMovieRating {
    private final AtomicInteger movieId;
    private AtomicLong rateSum;
    private AtomicInteger voteCount;

    public CachedMovieRating(AtomicInteger movieId) {
        this.movieId = movieId;
    }

    public CachedMovieRating(int movieId, double rateSum, int voteCount) {
        this.movieId = new AtomicInteger(movieId);
        this.rateSum = new AtomicLong(Double.doubleToLongBits(rateSum));
        this.voteCount = new AtomicInteger(voteCount);
    }

    public AtomicInteger getMovieId() {
        return movieId;
    }

    public AtomicLong getRateSum() {
        return rateSum;
    }

    public void setRateSum(AtomicLong rateSum) {
        this.rateSum = rateSum;
    }

    public AtomicInteger getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(AtomicInteger voteCount) {
        this.voteCount = voteCount;
    }
}
