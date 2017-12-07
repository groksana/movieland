package com.gromoks.movieland.dao;

import com.gromoks.movieland.dao.mapper.ReviewRowMapper;
import com.gromoks.movieland.entity.Movie;
import com.gromoks.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcReviewDao implements ReviewDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ReviewRowMapper reviewRowMapper = new ReviewRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String addReviewSQL;

    @Autowired
    private String getMovieToReviewSQL;

    @Override
    public Review addReview(Review review) {
        log.info("Start query to add review {} to DB", review);
        long startTime = System.currentTimeMillis();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("movieId", review.getMovieId());
        parameterSource.addValue("userId", review.getUser().getId());
        parameterSource.addValue("text", review.getText());
        namedParameterJdbcTemplate.update(addReviewSQL, parameterSource, keyHolder, new String[]{"id"});

        review.setId(keyHolder.getKey().intValue());
        log.info("Finish query to add review {} to DB. It took {} ms", review, System.currentTimeMillis() - startTime);

        return review;
    }

    @Override
    public void enrichSingleMovieByReview(Movie movie) {
        log.info("Start to enrich single movie by reviews");

        List<Review> reviews = getReviewListByMovie(movie);
        if (!Thread.currentThread().isInterrupted()) {
            movie.setReviews(reviews);
            log.info("Finish to enrich single movie by reviews");
        }
    }

    private List<Review> getReviewListByMovie(Movie movie) {
        List<Integer> movieIds = new ArrayList<>();
        movieIds.add(movie.getId());

        List<Review> reviews = jdbcTemplate.query(getMovieToReviewSQL, reviewRowMapper, movie.getId());

        return reviews;
    }
}
