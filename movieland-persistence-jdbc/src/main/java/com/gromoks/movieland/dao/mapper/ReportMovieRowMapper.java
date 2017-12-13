package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.entity.ReportMovie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportMovieRowMapper implements RowMapper<ReportMovie> {

    @Override
    public ReportMovie mapRow(ResultSet resultSet, int i) throws SQLException {
        ReportMovie reportMovie = new ReportMovie();
        reportMovie.setMovieId(resultSet.getInt("movieId"));
        reportMovie.setTitle(resultSet.getString("title"));
        reportMovie.setDescription(resultSet.getString("description"));
        reportMovie.setGenres(resultSet.getString("genres"));
        reportMovie.setPrice(resultSet.getDouble("price"));
        reportMovie.setRating(resultSet.getDouble("rating"));
        reportMovie.setReviewCount(resultSet.getInt("reviewCount"));

        return reportMovie;
    }
}
