package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MoviesGenres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movie_id")
    private int movieId;

    @Column(name="genre_id")
    private int genreId;
}
