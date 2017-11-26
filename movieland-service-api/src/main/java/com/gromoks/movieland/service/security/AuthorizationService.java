package com.gromoks.movieland.service.security;

import com.gromoks.movieland.service.entity.UserToken;

public interface AuthorizationService {
    void getAuthorizationAddReview(UserToken userToken);
}
