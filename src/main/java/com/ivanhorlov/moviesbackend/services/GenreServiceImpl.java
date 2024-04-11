package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.repositories.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;
    private final EntityManager entityManager;

    @Override
    public Genre getGenreByName(String name) {
        Optional<Genre> genreOptional = genreRepository.findGenreByName(name);
        if (genreOptional.isEmpty()){
            throw new NoSuchElementException(String.format("Genre %s not found", name));
        }
        return genreOptional.get();
    }

    @Override
    public Genre getGenreById(int id) {
        Optional<Genre> genreOptional = genreRepository.findGenreById(id);
        if (genreOptional.isEmpty()){
            throw new NoSuchElementException(String.format("Genre with id: %s not found", id));
        }
        return genreOptional.get();
    }

    @Override
    @Transactional
    public List<Genre> getAllGenres() {

        Query query = entityManager.createQuery("SELECT id, name FROM Genre ");
        List<Genre> genreList  = genreList = query.getResultList();

        return genreList;
    }

    @Override
    @Transactional
    public List<Integer> getAllIdsGenres() {

        Query query = entityManager.createQuery("SELECT id FROM Genre ");
        List<Integer> genreList  = genreList = query.getResultList();

        return genreList;
    }
}


