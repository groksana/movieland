package com.gromoks.movieland.dao.entity;

public class MovieRatingCache {
    private int movieId;
    private double rateSum;
    private int voteCount;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRateSum() {
        return rateSum;
    }

    public void setRateSum(double rateSum) {
        this.rateSum = rateSum;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
