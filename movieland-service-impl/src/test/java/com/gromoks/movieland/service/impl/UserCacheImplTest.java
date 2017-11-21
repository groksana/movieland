package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserCacheImplTest {
    @Mock
    UserDao mockUserDao;

    @InjectMocks
    UserCache userCache = new UserCacheImpl(mockUserDao);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setId(1);
        user.setNickname("Testnick");
        user.setEmail("test@email.com");
        user.setPassword("testpassword");
        when(mockUserDao.getUserByEmail("test@email.com")).thenReturn(user);
    }

    @Test
    public void testGetUserToken() {
        LoginRequest loginRequest = new LoginRequest("test@email.com","testpassword");
        UserToken userToken = new UserToken();
        userToken = userCache.getUserToken(loginRequest);
        assertEquals(userToken.getNickname(),"Testnick");
        assertNotNull(userToken.getUuid());
    }
}
