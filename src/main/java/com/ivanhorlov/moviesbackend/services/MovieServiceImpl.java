package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.MovieListResponse;
import com.ivanhorlov.moviesbackend.dtos.RequestGenresListDto;
import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.UserMovie;
import com.ivanhorlov.moviesbackend.exception_handling.NoSuchMovieException;
import com.ivanhorlov.moviesbackend.pagination.PaginationAndSorting;
import com.ivanhorlov.moviesbackend.pagination.SortingTypes;
import com.ivanhorlov.moviesbackend.repositories.MovieRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final PaginationAndSorting pagination;
    private final UserFavoriteMoviesServiceImpl userFavoriteMoviesService;
    private final EntityManager entityManager;


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
        Genre genre = genreService.getGenreByName(genreName);
        List<Movie> movieList = (List<Movie>)genre.getMoviesList();

        List<Integer> moviesIdsList = movieList.stream().map(Movie::getId).collect(Collectors.toList());

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public List<Integer> getMoviesIdsByGenre(int genreId, int pageNumber) {
        Genre genre = genreService.getGenreById(genreId);
        List<Movie> movieList = (List<Movie>)genre.getMoviesList();

        List<Integer> moviesIdsList = movieList.stream().map(Movie::getId).collect(Collectors.toList());

        return pagination.listPagination(moviesIdsList, 10, pageNumber);
    }

    @Override
    public MovieListResponse getMoviesByGenre(RequestGenresListDto genres, int pageNumber, SortingTypes sortingType) {
//        Genre genre = genreService.getGenreById(genreId);
//        List<Movie> movies = (List<Movie>) genre.getMoviesList();

        Query query = entityManager.createQuery("SELECT movie FROM Movie movie " +
                " INNER JOIN " +
                        " MoviesGenres movieGenres " +
                        "ON movie.id= movieGenres.movieId" +
                " WHERE movieGenres.genreId IN (:genres) GROUP BY movie.id HAVING COUNT(movie.id) = :genresCount");


//        Query query = entityManager.createQuery("SELECT movieId FROM MoviesGenres " +
//                "WHERE genreId IN (:genres) GROUP BY movieId HAVING COUNT(movieId) = 2");

        query.setParameter("genres", genres.getGenresIds());
        query.setParameter("genresCount", genres.getGenresIds().size());

        List<Movie> movies = new ArrayList<>();

        switch (sortingType) {
            case popularity_desc -> movies = pagination.sortByPopularity(query.getResultList(), sortingType);
            case popularity_asc -> movies = pagination.sortByPopularity(query.getResultList(), sortingType);
            case release_date_desc -> movies = pagination.sortByReleaseDate(query.getResultList(), sortingType);
            case release_date_asc -> movies = pagination.sortByReleaseDate(query.getResultList(), sortingType);
            case rating_desc -> movies = pagination.sortByRating(query.getResultList(), sortingType);
            case rating_asc -> movies = pagination.sortByRating(query.getResultList(), sortingType);

            default -> {
                movies = pagination.sortByPopularity(query.getResultList(), sortingType);
            }
        }


        MovieListResponse response = new MovieListResponse();
        response.setMovie_list(pagination.listPagination(movies, 12, pageNumber));
        response.setTotal_pages(movies.size());

        return response;
    }

    @Override
    public MovieListResponse getMoviesByPopularity(int amount, int pageNumber) {
        Query query = entityManager.createQuery("from Movie");
        List<Movie> movieList = query.getResultList();

        List<Movie> sortedList = movieList.stream().sorted((movieA, movieB) -> (int) (movieB.getPopularity() - movieA.getPopularity())).toList();

        List<Movie> paginatedList = pagination.listPagination(sortedList, amount, pageNumber);

        MovieListResponse response = new MovieListResponse();
        response.setMovie_list(paginatedList);
        response.setTotal_pages(movieList.size());

        return response;
    }

    @Override
    public List<Movie> getAllFavoriteMoviesByUserId(int userId) {
        List<UserMovie> userMovieList = userFavoriteMoviesService.getAllFavoritesMoviesByUserId(userId);
        List<Movie> movieList = userMovieList.stream().map(userMovie -> getMovieById(userMovie.getMovieId()))
                .collect(Collectors.toList());

        return movieList;
    }
}
