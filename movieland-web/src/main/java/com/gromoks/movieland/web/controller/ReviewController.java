package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.service.security.AuthorizationService;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

import static com.gromoks.movieland.web.util.JsonJacksonConverter.toJsonReview;

@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReviewController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addReview(@RequestHeader(value = "uuid") String uuid, @RequestBody String json) throws AuthenticationException {
        log.info("Sending request to add new review {}", json);
        long startTime = System.currentTimeMillis();

        User user = authenticationService.getAuthenticatedUser();

        authorizationService.authorizeToAddReview(user);
        Review review = JsonJacksonConverter.parseReview(json);
        review.setUser(user);
        Review addedReview = reviewService.addReview(review);
        String reviewJson = toJsonReview(addedReview);

        log.info("Review has been added. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(reviewJson,HttpStatus.OK);
    }

}
