package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GenreService {
    public Genre findGenreByName(String name);
    public Genre findGenreById(int id);
}
