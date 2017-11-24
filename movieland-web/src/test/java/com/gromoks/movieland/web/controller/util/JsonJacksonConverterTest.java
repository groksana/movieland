package com.gromoks.movieland.web.controller.util;

import com.gromoks.movieland.entity.Review;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonJacksonConverterTest {
    @Test
    public void testConvertJsonToUserRequest() {
        String json = "{\"movieId\":1,\"text\":\"testText\"}";;
        Review review = JsonJacksonConverter.parseReview(json);
        assertEquals(review.getMovieId(),1);
        assertEquals(review.getText(),"testText");
    }
}
