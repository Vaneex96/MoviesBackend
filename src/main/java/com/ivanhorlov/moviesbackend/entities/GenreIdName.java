package com.ivanhorlov.moviesbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="genres")
@Data
public class GenreIdName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;
}
