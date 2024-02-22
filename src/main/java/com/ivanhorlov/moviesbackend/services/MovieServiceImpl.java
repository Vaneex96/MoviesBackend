package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.UserMovie;
import com.ivanhorlov.moviesbackend.exception_handling.NoSuchMovieException;
import com.ivanhorlov.moviesbackend.pagination.Pagination;
import com.ivanhorlov.moviesbackend.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final Pagination pagination;
    private final UserFavoriteMoviesServiceImpl userFavoriteMoviesService;

    @Override
    public Movie getMovieById(int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isEmpty()){
            throw new NoSuchMovieException("Movie with id: " + id + " not found");
        }
        return optionalMovie.get();
    }

    @Override
    public Movie getMovieByTitle(String title) {
        Optional<Movie> optionalMovie = movieRepository.findMovieByTitle(title);
        if(optionalMovie.isEmpty()){
            throw new NoSuchMovieException("Movie with title: " + title + " not found");
        }

        return optionalMovie.get();
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber) {
        Genre genre = genreService.findGenreByName(genreName);
        List<Movie> movieList = genre.getMoviesList();

        List<Integer> moviesIdsList = movieList.stream().map(Movie::getId).collect(Collectors.toList());

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber) {
        Genre genre = genreService.findGenreById(genreId);
        List<Movie> movieList = genre.getMoviesList();

        List<Integer> moviesIdsList = movieList.stream().map(Movie::getId).collect(Collectors.toList());

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, int pageNumber) {
        Genre genre = genreService.findGenreById(genreId);
        List<Movie> movies = genre.getMoviesList();

        return pagination.listPagination(movies, 2, pageNumber);
    }

    @Override
    public List<Movie> getMoviesByPopularity(int amount, int pageNumber) {
        return null;
    }

    @Override
    public List<Movie> getAllFavoriteMoviesByUserId(int userId) {
        List<UserMovie> userMovieList = userFavoriteMoviesService.getAllFavoritesMoviesByUserId(userId);
        List<Movie> movieList = userMovieList.stream().map(userMovie -> getMovieById(userMovie.getMovieId()))
                .collect(Collectors.toList());

        return movieList;
    }
}
