package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Movie;
import java.util.List;

public interface MovieService {

    public Movie getMovieById(int id);

    public Movie getMovieByTitle(String title);

    public List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber);

    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber);

    public List<Movie> getMoviesByGenre(int genreId, int pageNumber);

    public List<Movie> getMoviesByPopularity(int amount, int pageNumber);

    public List<Movie> getAllFavoriteMoviesByUserId(int userId);
}
