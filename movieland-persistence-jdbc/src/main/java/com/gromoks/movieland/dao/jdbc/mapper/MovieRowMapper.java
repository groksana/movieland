package com.gromoks.movieland.dao.jdbc.mapper;

import com.gromoks.movieland.entity.Movie;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRowMapper implements RowMapper<Movie> {

    @Override
    public Movie mapRow(ResultSet resultSet, int i) throws SQLException {

        Movie movie = new Movie();
        movie.setId(resultSet.getInt("id"));
        movie.setNameRussian(resultSet.getString("nameRussian"));
        movie.setNameNative(resultSet.getString("nameNative"));
        movie.setYearOfRelease(resultSet.getInt("yearOfRelease"));
        movie.setDescription(resultSet.getString("description"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setPicturePath(resultSet.getString("picturePath"));

        return movie;
    }
}
