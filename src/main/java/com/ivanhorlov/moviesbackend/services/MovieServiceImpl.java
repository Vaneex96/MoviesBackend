package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.UserMovie;
import com.ivanhorlov.moviesbackend.exception_handling.NoSuchMovieException;
import com.ivanhorlov.moviesbackend.pagination.Pagination;
import com.ivanhorlov.moviesbackend.repositories.GenreRepository;
import com.ivanhorlov.moviesbackend.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final Pagination pagination;
    private final UserFavoriteMoviesServiceImpl userFavoriteMoviesService;

    @Override
    public Movie getMovieById(int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        Movie movie = null;
        if (optionalMovie.isPresent()){
            movie = optionalMovie.get();
        } else {
            throw new NoSuchMovieException("Movie with id: " + id + " not found");
        }

        return movie;
    }

    @Override
    public Movie getMovieByTitle(String title) {
        Optional<Movie> optionalMovie = movieRepository.findMovieByTitle(title);
        Movie movie = null;
        if (optionalMovie.isPresent()){
            movie = optionalMovie.get();
        } else {
            throw new NoSuchMovieException("Movie with title: " + title + " not found");
        }

        return movie;
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber) {
        Optional<Genre> optionalGenre = genreRepository.findGenreByName(genreName);
        List<Movie> movieList = new ArrayList<>();
        if (optionalGenre.isPresent()){
            movieList = optionalGenre.get().getMoviesList();
        }else{
            throw new NoSuchElementException(String.format("Genre with name = %s not found", genreName));
        }

        List<Integer> moviesIdsList = new ArrayList<>();

        movieList.stream().forEach(movie -> moviesIdsList.add(movie.getId()));

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber) {
        Optional<Genre> optionalGenre = genreRepository.findGenreById(genreId);
        List<Movie> movieList = new ArrayList<>();
        if (optionalGenre.isPresent()){
            movieList = optionalGenre.get().getMoviesList();
        } else {
            throw new NoSuchElementException(String.format("Genre with id = %s not found", genreId));
        }

        List<Integer> moviesIdsList = new ArrayList<>();
        movieList.forEach(movie -> moviesIdsList.add(movie.getId()));

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, int pageNumber) {
        Optional<Genre> genre = genreRepository.findGenreById(genreId);
        List<Movie> movies = new ArrayList<>();

        if(genre.isPresent()){
            movies = genre.get().getMoviesList();
        }else{
            throw new NoSuchElementException(String.format("Genre with id = %s not found", genreId));
        }

        return pagination.listPagination(movies, 2, pageNumber);
    }

    @Override
    public List<Movie> getMoviesByPopularity(int amount, int pageNumber) {
        return null;
    }

    @Override
    public List<Movie> getAllFavoriteMoviesByUserId(int userId) {
        List<UserMovie> userMovieList = userFavoriteMoviesService.getAllFavoritesMoviesByUserId(userId);
        List<Movie> movieList = new ArrayList<>();

        for(UserMovie item: userMovieList){
            Movie movie = getMovieById(item.getMovieId());
            movieList.add(movie);
        }

        return movieList;
    }
}
