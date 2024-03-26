package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.MovieListResponse;
import com.ivanhorlov.moviesbackend.dtos.RequestGenresListDto;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.pagination.SortingTypes;

import java.util.List;

public interface MovieService {

    Movie getMovieById(int id);

    Movie getMovieByTitle(String title);

    List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber);

    List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber);

    MovieListResponse getMoviesByTitle(String title, int pageNumber, int paginateBy, SortingTypes sortingType);

    List<Movie> getMoviesByTitleQuery(String title, int pageNumber, int paginateBy, SortingTypes sortingTypes);

    MovieListResponse getMoviesByGenre(RequestGenresListDto genres, int pageNumber, SortingTypes sortingType,int paginateBy);

    MovieListResponse getMoviesByPopularity(int amount, int pageNumber);

    List<Movie> getAllFavoriteMoviesByUserId(int userId);
}
