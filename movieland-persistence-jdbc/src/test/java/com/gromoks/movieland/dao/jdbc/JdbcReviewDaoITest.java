package com.gromoks.movieland.dao.jdbc;

import com.gromoks.movieland.dao.config.JdbcConfig;
import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {JdbcConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcReviewDaoITest {

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Test
    public void testAddReview() {
        Review review = new Review();
        int movieId = 1;
        review.setMovieId(movieId);
        review.setUser(new User(1));
        String text = "Test Waw";
        review.setText(text);
        reviewDao.addReview(review);

        String removeTestDataSql = "DELETE FROM review WHERE movieId = :movieId AND text = :text";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("movieId",movieId);
        parameterSource.addValue("text",text);
        namedJdbcTemplate.update(removeTestDataSql,parameterSource);
    }

}
