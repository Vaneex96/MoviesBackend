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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Transactional
    public MovieListResponse getMoviesByTitle(String title, int pageNumber, int paginateBy, SortingTypes sortingType) {

        List<Movie> movieList =  new ArrayList<>();

        while (movieList.size() == 0 && title.length() > 2){
            movieList = getMoviesByTitleQuery("'%" + title + "%'", pageNumber, paginateBy, sortingType);
            if(movieList.size() == 0) title = title.substring(0, title.length() - 1);
        }

        long totalPages = getMoviesByTitleQuery("'%" + title + "%'", 0, 0, null).size()/paginateBy;

        MovieListResponse response = new MovieListResponse();
        response.setMovie_list(movieList);
        response.setTotal_pages(totalPages);

        return response;
    }

    @Override
    @Transactional
    public List<Movie> getMoviesByTitleQuery(String title, int pageNumber, int paginateBy, SortingTypes sortingType) {

        if(sortingType == null) sortingType = SortingTypes.popularity_desc;
        if(paginateBy == 0) paginateBy = 100;
        if(pageNumber == 0) pageNumber = 1;

        String orderBy = sortingType.toString().replaceFirst("_desc", " desc").replaceFirst("_asc", " asc");

        Query query = entityManager.createQuery(String.format("SELECT movie FROM Movie movie " +
                        "WHERE movie.title LIKE %s " +
                        "ORDER BY movie.%s " +
                        "LIMIT %s OFFSET %s ", title, orderBy, paginateBy, (pageNumber - 1) * paginateBy)
                );

        return query.getResultList();
    }


    @Override
    @Transactional
    public MovieListResponse getMoviesByGenre(RequestGenresListDto genres, int pageNumber, SortingTypes sortingType,int paginateBy) {

        String orderBy = sortingType.toString().replaceFirst("_desc", " desc").replaceFirst("_asc", " asc");
        String genresIds = genres.getGenresIds().toString().replace("[", "(").replace("]", ")");

        String hqlTemplate;
        Query queryTotalPages;
        long totalPages;

        if(genres.getGenresIds().size() == 0){
            hqlTemplate = String.format("SELECT movie FROM Movie movie " +
                    "ORDER BY movie.%s " +
                    "LIMIT %s OFFSET %s ", orderBy, paginateBy, (pageNumber - 1) * paginateBy);

            queryTotalPages = entityManager.createQuery("SELECT COUNT(*) FROM Movie movie ");

            totalPages = queryTotalPages.getResultList().size();

        } else {
            hqlTemplate = String.format("SELECT movie FROM Movie movie INNER JOIN MoviesGenres movieGenres " +
                    "ON movie.id= movieGenres.movieId " +
                    "WHERE movieGenres.genreId IN %s " +
                    "GROUP BY movie.id " +
                    "HAVING COUNT(movie.id) = %s " +
                    "ORDER BY movie.%s " +
                    "LIMIT %s OFFSET %s ", genresIds, genres.getGenresIds().size(), orderBy, paginateBy, (pageNumber - 1) * paginateBy);

            queryTotalPages = entityManager.createQuery("SELECT COUNT(*) FROM Movie movie " +
                    " INNER JOIN MoviesGenres movieGenres " +
                    "ON movie.id= movieGenres.movieId " +
                    "WHERE movieGenres.genreId IN (:genres) " +
                    "GROUP BY movie.id " +
                    "HAVING COUNT(movie.id) = :genresCount "
            );

            queryTotalPages.setParameter("genres", genres.getGenresIds());
            queryTotalPages.setParameter("genresCount", genres.getGenresIds().size());

            totalPages = (long) queryTotalPages.getResultList().get(0);
        }

        Query queryMovies = entityManager.createQuery(hqlTemplate);
        List<Movie> movies = queryMovies.getResultList();

        MovieListResponse response = new MovieListResponse();
        response.setMovie_list(movies);
        response.setTotal_pages(totalPages);

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
