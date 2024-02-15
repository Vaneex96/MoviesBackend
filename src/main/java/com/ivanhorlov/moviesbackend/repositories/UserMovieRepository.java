package com.ivanhorlov.moviesbackend.repositories;

import com.ivanhorlov.moviesbackend.entities.UserMovie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserMovieRepository extends CrudRepository<UserMovie, Integer> {
    public Optional<List<UserMovie>> findUserMovieByUserId(int userId);
}
