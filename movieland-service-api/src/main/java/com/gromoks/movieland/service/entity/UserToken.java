package com.gromoks.movieland.service.entity;

import com.gromoks.movieland.entity.User;

public class UserToken {
    private String uuid;
    private User user;
    private long expireTimeInMs;

    public UserToken() {}

    public UserToken(String uuid, User user, long expireTimeInMs) {
        this.uuid = uuid;
        this.user = user;
        this.expireTimeInMs = expireTimeInMs;
    }

    public long getExpireTimeInMs() {
        return expireTimeInMs;
    }

    public void setExpireTimeInMs(long expireTimeInMs) {
        this.expireTimeInMs = expireTimeInMs;
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
