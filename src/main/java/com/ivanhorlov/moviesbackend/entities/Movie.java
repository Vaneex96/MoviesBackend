package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="movies")
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="adult")
    private boolean adult;

    @Column(name="backdrop_path")
    private String backdropPath;

    @Column(name="original_language")
    private String originalLang;

    @Column(name="original_title")
    private String originalTitle;

    @Column(name="overview")
    private String overview;

    @Column(name="popularity")
    private double popularity;

    @Column(name="poster_path")
    private String poster_path;

    @Column(name="release_date")
    private Date release_date;

    @Column(name="title")
    private String title;

    @Column(name="video")
    private String video;

    @Column(name="trailer")
    private String trailer;

    @Column(name="vote_average")
    private int voteAverage;

    @Column(name="vote_count")
    private int voteCount;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name="movies_genres",
            joinColumns = @JoinColumn(name="movie_id"),
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    private List<GenreIdName> genres;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", originalLang='" + originalLang + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity='" + popularity + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", release_date=" + release_date +
                ", title='" + title + '\'' +
//                ", genresList=" + genresList +
                '}';
    }


}
