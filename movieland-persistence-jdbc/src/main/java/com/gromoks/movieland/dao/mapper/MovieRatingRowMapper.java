package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.dao.entity.CachedMovieRating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MovieRatingRowMapper implements RowMapper<CachedMovieRating> {

    @Override
    public CachedMovieRating mapRow(ResultSet resultSet, int i) throws SQLException {

        CachedMovieRating cachedMovieRating = new CachedMovieRating(new AtomicInteger(resultSet.getInt("movieId")));
        cachedMovieRating.setRateSum(new AtomicLong(Double.doubleToLongBits(resultSet.getDouble("rateSum"))));
        cachedMovieRating.setVoteCount(new AtomicInteger(resultSet.getInt("voteCount")));
        return cachedMovieRating;
    }
}
