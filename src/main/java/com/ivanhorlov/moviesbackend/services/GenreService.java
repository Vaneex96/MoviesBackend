package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenreService {
    Genre getGenreByName(String name);
    Genre getGenreById(int id);
    List<Genre> getAllGenres();
    List<Integer> getAllIdsGenres();
}
