package com.gromoks.movieland.dao.jdbc.mapper;

import com.gromoks.movieland.dao.entity.MovieRatingCache;
import com.gromoks.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRatingRowMapper implements RowMapper<MovieRatingCache> {

    @Override
    public MovieRatingCache mapRow(ResultSet resultSet, int i) throws SQLException {

        MovieRatingCache movieRatingCache = new MovieRatingCache();
        movieRatingCache.setMovieId(resultSet.getInt("movieId"));
        movieRatingCache.setRateSum(resultSet.getDouble("rateSum"));
        movieRatingCache.setVoteCount(resultSet.getInt("voteCount"));
        return movieRatingCache;
    }
}
