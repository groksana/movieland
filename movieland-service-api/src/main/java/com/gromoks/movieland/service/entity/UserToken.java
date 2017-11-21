package com.gromoks.movieland.service.entity;

public class UserToken {
    private String uuid;
    private String nickname;
    private String email;
    private long initTimeInMs;

    public UserToken() {}

    public UserToken(String uuid, String nickname, String email, long initTimeInMs) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.email = email;
        this.initTimeInMs = initTimeInMs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getInitTimeInMs() {
        return initTimeInMs;
    }

    public void setInitTimeInMs(long initTimeInMs) {
        this.initTimeInMs = initTimeInMs;
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
