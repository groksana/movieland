package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.dao.entity.MovieToReview;
import com.gromoks.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieToReviewRowMapper implements RowMapper<MovieToReview> {

    @Override
    public MovieToReview mapRow(ResultSet resultSet, int i) throws SQLException {
        MovieToReview movieToReview = new MovieToReview();
        movieToReview.setId(resultSet.getInt("id"));
        movieToReview.setMovieId(resultSet.getInt("movieId"));
        movieToReview.setText(resultSet.getString("text"));
        movieToReview.setUser(new User(resultSet.getInt("userId"), resultSet.getString("nickname")));

        return movieToReview;
    }
}
