package com.gromoks.movieland.web.entity;

import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

public class UserTokenDto {

    @JsonView(UserTokenViews.Normal.class)
    private String uuid;

    @JsonView(UserTokenViews.Normal.class)
    private String nickname;

    @JsonView(UserTokenViews.Full.class)
    private String email;

    @JsonView(UserTokenViews.Full.class)
    private LocalDateTime expireDateTime;

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

    public LocalDateTime getExpireDateTime() {
        return expireDateTime;
    }

    public void setExpireDateTime(LocalDateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }
}
