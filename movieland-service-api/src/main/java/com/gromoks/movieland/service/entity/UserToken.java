package com.gromoks.movieland.service.entity;

import com.gromoks.movieland.entity.User;

import java.time.LocalDateTime;

public class UserToken {
    private String uuid;
    private User user;
    private LocalDateTime expireDateTime;

    public UserToken() {
    }

    public UserToken(String uuid, User user, LocalDateTime expireDateTime) {
        this.uuid = uuid;
        this.user = user;
        this.expireDateTime = expireDateTime;
    }

    public LocalDateTime getExpireDateTime() {
        return expireDateTime;
    }

    public void setExpireDateTime(LocalDateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
