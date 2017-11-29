package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.service.ReviewService;
import com.gromoks.movieland.web.handler.GlobalControllerExceptionHandler;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewControllerTest {

    @Mock
    private AuthenticationService mockAuthenticationService;

    @Mock
    private ReviewService mockReviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public void testAddReview() throws Exception {

        User user = new User(1,"TestNickname","test@email.com","USER");

        String json = "{\"movieId\":1,\"text\":\"testText\"}";
        String uuid = "12345";

        Review review = JsonJacksonConverter.parseReview(json);
        review.setUser(new User(1,"TestNickname","test@email.com","USER"));
        review.setId(1);
        Review addedReview = new Review();
        addedReview.setId(1);
        addedReview.setMovieId(1);
        addedReview.setUser(new User(1,"TestNickname","test@email.com","USER"));
        addedReview.setText("testText");

        when(mockAuthenticationService.getAuthenticatedUser()).thenReturn(user);
        when(mockReviewService.addReview(review)).thenReturn(addedReview);

        mockMvc.perform(post("/review")
                .contentType(MediaType.APPLICATION_JSON)
                .header("uuid",uuid)
                .content(json))
                .andExpect(status().isOk());

    }
}
