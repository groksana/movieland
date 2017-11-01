package com.gromoks.movieland.dao.entity;

public class MovieToGenre {
    private int movieId;
    private int genreId;
    private String genre;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MovieToGenre{" +
                "movieId=" + movieId +
                ", genreId=" + genreId +
                ", genre='" + genre + '\'' +
                '}';
    }
}
