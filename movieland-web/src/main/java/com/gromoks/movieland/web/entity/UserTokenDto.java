package com.gromoks.movieland.web.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class UserTokenDto {

    @JsonView(UserTokenViews.Normal.class)
    private String uuid;

    @JsonView(UserTokenViews.Normal.class)
    private String nickname;

    @JsonView(UserTokenViews.Full.class)
    private String email;

    @JsonView(UserTokenViews.Full.class)
    private long initTimeInMs;

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
}
