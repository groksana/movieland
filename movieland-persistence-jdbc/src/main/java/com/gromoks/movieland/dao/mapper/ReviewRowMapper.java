package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet resultSet, int i) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getInt("id"));
        review.setMovieId(resultSet.getInt("movieId"));
        review.setText(resultSet.getString("text"));
        review.setUser(new User(resultSet.getInt("userId"), resultSet.getString("nickname")));

        return review;
    }
}
