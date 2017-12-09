package com.gromoks.movieland.dao.mapper;

import com.gromoks.movieland.entity.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {

    @Override
    public Country mapRow(ResultSet resultSet, int i) throws SQLException {
        Country country = new Country();
        country.setId(resultSet.getInt("id"));
        country.setName(resultSet.getString("country"));

        return country;
    }
}
