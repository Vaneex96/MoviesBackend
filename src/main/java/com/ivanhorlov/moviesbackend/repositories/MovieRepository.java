package com.ivanhorlov.moviesbackend.repositories;

import com.ivanhorlov.moviesbackend.entities.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    public Optional<Movie> findMovieByTitle(String title);
}
