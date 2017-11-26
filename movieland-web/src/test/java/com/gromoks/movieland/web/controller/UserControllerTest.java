package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.web.handler.GlobalControllerExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Mock
    private AuthenticationService mockAuthenticationService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
    }

    @Test
    public void testLogin() throws Exception {

        UserToken userToken = new UserToken();
        userToken.setUuid("12345");
        userToken.setUser(new User(1,"TestNickname","test@email.com","USER"));
        userToken.setExpireDateTime(LocalDateTime.now().plusHours(2));

        String jsonLoginRequest = "{\"email\":\"test@email.com\",\"password\":\"testpassword\"}";

        when(mockAuthenticationService.getAuthentication(jsonLoginRequest)).thenReturn(userToken);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname", is("TestNickname")))
                .andExpect(jsonPath("$.uuid", is("12345")));
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(delete("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("uuid","12345"))
                .andExpect(status().isOk());

    }
}
