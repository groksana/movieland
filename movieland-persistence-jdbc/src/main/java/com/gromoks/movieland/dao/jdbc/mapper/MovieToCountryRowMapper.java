package com.gromoks.movieland.dao.jdbc.mapper;

import com.gromoks.movieland.dao.entity.MovieToCountry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieToCountryRowMapper implements RowMapper<MovieToCountry> {

    @Override
    public MovieToCountry mapRow(ResultSet resultSet, int i) throws SQLException {

        MovieToCountry movieToCountry = new MovieToCountry();
        movieToCountry.setMovieId(resultSet.getInt("movieId"));
        movieToCountry.setCountryId(resultSet.getInt("id"));
        movieToCountry.setCountry(resultSet.getString("country"));

        return movieToCountry;
    }
}
