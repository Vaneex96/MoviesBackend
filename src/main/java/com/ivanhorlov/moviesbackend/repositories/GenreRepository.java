package com.ivanhorlov.moviesbackend.repositories;

import com.ivanhorlov.moviesbackend.entities.Genre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Integer> {

    public Optional<Genre> findGenreByName(String name);

    public Optional<Genre> findGenreById(int id);

}
