package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Table(name="genres")
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;


    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name="movies_genres",
            joinColumns = @JoinColumn(name="genre_id"),
            inverseJoinColumns = @JoinColumn(name="movie_id")
    )
    private Collection<Movie> moviesList;


    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", moviesList=" + moviesList +
                '}';
    }
}
