package com.gromoks.movieland.service.entity;

public class UserToken {
    private String uuid;
    private String nickname;
    private String email;
    private long expireTimeInMs;

    public UserToken() {}

    public UserToken(String uuid, String nickname, String email, long expireTimeInMs) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.email = email;
        this.expireTimeInMs = expireTimeInMs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
