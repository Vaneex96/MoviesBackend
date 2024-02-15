package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users_movies")
public class UserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Column(name="movie_id")
    private int movieId;

    public UserMovie() {
    }

    public UserMovie(int userId, int movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }
}
