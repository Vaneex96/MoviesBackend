package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.MovieListResponse;
import com.ivanhorlov.moviesbackend.dtos.RequestGenresListDto;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.pagination.SortingTypes;

import java.util.List;

public interface MovieService {

    public Movie getMovieById(int id);

    public Movie getMovieByTitle(String title);

    public List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber);

    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber);

    MovieListResponse getMoviesByGenre(RequestGenresListDto genres, int pageNumber, SortingTypes sortingType);

    public MovieListResponse getMoviesByPopularity(int amount, int pageNumber);

    public List<Movie> getAllFavoriteMoviesByUserId(int userId);
}
