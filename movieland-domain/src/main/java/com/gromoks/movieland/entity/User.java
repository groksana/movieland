package com.gromoks.movieland.entity;

public class User {
    private int id;
    private String nickname;
    private String email;
    private String role;

    public User() {}

    public User(int id) {
        this.id = id;
    }

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public User(int id, String nickname, String email, String role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
