package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.ReviewDao;
import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public void addReview(Review review) {
        reviewDao.addReview(review);
    }
}
