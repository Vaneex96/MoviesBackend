package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    public Genre findGenreByName(String name) {
        Optional<Genre> genreOptional = genreRepository.findGenreByName(name);
        if (genreOptional.isEmpty()){
            throw new NoSuchElementException(String.format("Genre %s not found", name));
        }
        return genreOptional.get();
    }

    @Override
    public Genre findGenreById(int id) {
        Optional<Genre> genreOptional = genreRepository.findGenreById(id);
        if (genreOptional.isEmpty()){
            throw new NoSuchElementException(String.format("Genre with id: %s not found", id));
        }
        return genreOptional.get();
    }
}
