package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.service.AuthenticationService;
import com.gromoks.movieland.service.AuthorizationService;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addReview(@RequestHeader(value = "uuid") String uuid,@RequestBody String json) {
        log.info("Sending request to add new review {}", json);
        long startTime = System.currentTimeMillis();

        UserToken userToken = authenticationService.getAuthenticationByUuid(uuid);
        authorizationService.getAuthorizationAddReview(userToken);
        Review review = JsonJacksonConverter.parseReview(json);
        review.setUser(userToken.getUser());
        reviewService.addReview(review);

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
