package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.UserMovie;
import com.ivanhorlov.moviesbackend.repositories.UserMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFavoriteMoviesServiceImpl implements UsersFavoriteMoviesService {

    private final UserMovieRepository userMovieRepository;


    @Override
    public List<UserMovie> getAllFavoritesMoviesByUserId(int id) {
        Optional<List<UserMovie>> userMovieOptional = userMovieRepository.findUserMovieByUserId(id);
        if(userMovieOptional.isEmpty()){
            throw new NoSuchElementException("User with id: " + id + " doesn't have favorite movies");
        }

        return userMovieOptional.get();
    }

    @Override
    public void addMovieToFavorite(UserMovie userMovie) {
        UserMovie userMovieMy = new UserMovie(3, 897087);
        userMovieRepository.save(userMovieMy);
    }
}
