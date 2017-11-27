package com.gromoks.movieland.service.impl.util;

import com.gromoks.movieland.service.entity.LoginRequest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonUserRequestConverterTest {

    @Test
    public void testConvertJsonToUserRequest() {
        String jsonLoginRequest = "{\"email\":\"test@email.com\",\"password\":\"testpassword\"}";;
        LoginRequest loginRequest = JsonUserRequestConverter.convertJsonToUserRequest(jsonLoginRequest);
        assertEquals(loginRequest.getEmail(),"test@email.com");
        assertEquals(loginRequest.getPassword(),"testpassword");
    }
}
