package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.dao.entity.MovieToGenre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieToGenreRowMapper implements RowMapper<MovieToGenre> {
    @Override
    public MovieToGenre mapRow(ResultSet resultSet, int i) throws SQLException {

        MovieToGenre movieToGenre = new MovieToGenre();
        movieToGenre.setMovieId(resultSet.getInt("movieId"));
        movieToGenre.setGenreId(resultSet.getInt("id"));
        movieToGenre.setGenre(resultSet.getString("genre"));

        return movieToGenre;
    }
}
