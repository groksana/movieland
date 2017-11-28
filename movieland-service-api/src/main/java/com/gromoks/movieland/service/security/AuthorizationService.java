package com.gromoks.movieland.service.security;

import com.gromoks.movieland.entity.User;

public interface AuthorizationService {
    void authorizeToAddReview(User user);
}
