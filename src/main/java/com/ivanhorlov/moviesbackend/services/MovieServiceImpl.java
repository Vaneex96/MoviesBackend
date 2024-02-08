package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.pagination.Pagination;
import com.ivanhorlov.moviesbackend.repositories.GenreRepository;
import com.ivanhorlov.moviesbackend.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Override
    public Movie getMovieById(int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        Movie movie = null;
        if (optionalMovie.isPresent()){
            movie = optionalMovie.get();
        }

        return movie;
    }

    @Override
    public Movie getMovieByTitle(String title) {
        Optional<Movie> optionalMovie = movieRepository.findMovieByTitle(title);
        Movie movie = null;
        if (optionalMovie.isPresent()){
            movie = optionalMovie.get();
        }

        return movie;
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(String genreName, int pageNumber) {
        Optional<Genre> optionalGenre = genreRepository.findGenreByName(genreName);
        List<Movie> movieList = new ArrayList<>();
        if (optionalGenre.isPresent()){
            movieList = optionalGenre.get().getMoviesList();
        }

        List<Integer> moviesIdsList = new ArrayList<>();

        movieList.stream().forEach(movie -> moviesIdsList.add(movie.getId()));

        return moviesIdsList;
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber) {
        Optional<Genre> optionalGenre = genreRepository.findGenreById(genreId);
        List<Movie> movieList = new ArrayList<>();
        if (optionalGenre.isPresent()){
            movieList = optionalGenre.get().getMoviesList();
        }

        List<Integer> moviesIdsList = new ArrayList<>();
        movieList.forEach(movie -> moviesIdsList.add(movie.getId()));

        Pagination<Integer> pagination = new Pagination<>();
        pagination.setPage(pageNumber);

        List<Integer> paginatedMoviesList = pagination.listPagination(moviesIdsList);

        return paginatedMoviesList;
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, int pageNumber) {
        Optional<Genre> genre = genreRepository.findGenreById(genreId);
        List<Movie> movies = new ArrayList<>();

        if(genre.isPresent()){
            movies = genre.get().getMoviesList();
        }

        Pagination<Movie> pagination = new Pagination<>();
        pagination.setPage(pageNumber);

        List<Movie> paginatedMoviesList = pagination.listPagination(movies);

        return paginatedMoviesList;
    }

    @Override
    public List<Movie> getMoviesByPopularity(int amount, int pageNumber) {
        return null;
    }
}
