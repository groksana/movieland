package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReviewDao implements ReviewDao{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String addReviewSQL;

    @Override
    public void addReview(Review review) {
        log.info("Start query to add review {} to DB", review);
        long startTime = System.currentTimeMillis();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("movieId",review.getMovieId());
        parameterSource.addValue("userId",review.getUser().getId());
        parameterSource.addValue("text",review.getText());
        namedParameterJdbcTemplate.update(addReviewSQL,parameterSource);
        log.info("Finish query to add review {} to DB. It took {} ms", review, System.currentTimeMillis() - startTime);
    }
}
