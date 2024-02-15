package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.UserMovie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersFavoriteMoviesService {
    public List<UserMovie> getAllFavoritesMoviesByUserId(int id);
    public void addMovieToFavorite(UserMovie userMovie);
}
