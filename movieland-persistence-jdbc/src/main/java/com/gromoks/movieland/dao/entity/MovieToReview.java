package com.gromoks.movieland.dao.entity;

import com.gromoks.movieland.entity.User;

public class MovieToReview {
    private int id;
    private int movieId;
    private User user;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MovieToReview{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", user=" + user +
                ", text='" + text + '\'' +
                '}';
    }
}
