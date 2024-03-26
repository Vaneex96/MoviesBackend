package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.dtos.MovieListResponse;
import com.ivanhorlov.moviesbackend.dtos.RequestGenresListDto;
import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.pagination.SortingTypes;
import com.ivanhorlov.moviesbackend.services.GenreService;
import com.ivanhorlov.moviesbackend.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final MovieService movieService;
    private final GenreService genreService;


    @GetMapping("/movie/id/{id}")
    public Movie getMovieById(@PathVariable int id){
        return movieService.getMovieById(id);
    }

    @GetMapping("/movie/title/{title}")
    public Movie getMovieByTitle(@PathVariable String title){
        return movieService.getMovieByTitle(title);
    }

    @GetMapping("/ids/genrename/{genreName}/pagenumber/{pagenumber}")
    public List<Integer> getMoviesIdsByGenreName(@PathVariable String genreName, @PathVariable int pagenumber){
        return movieService.getMoviesIdsByGenre(genreName, pagenumber);
    }

    @GetMapping("/ids/genreid/{id}/page/{page}")
    public List<Integer> getMoviesIdsByGenreId(@PathVariable int id, @PathVariable int page){
        return movieService.getMoviesIdsByGenre(id, page);
    }

    @PostMapping("/by_genres_ids/page/{page}")
    public MovieListResponse getMoviesByGenreId(@RequestBody RequestGenresListDto genres, @PathVariable int page){
        return movieService.getMoviesByGenre(genres, page, genres.getSortingType(), 12);
    }

    @GetMapping("/search_by_title/{title}/page/{page}/paginate_by/{paginateBy}/sort_by/{sort_by}")
    public ResponseEntity<MovieListResponse> getMoviesByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int paginateBy, @PathVariable SortingTypes sort_by){
        MovieListResponse data = movieService.getMoviesByTitle(title, page, paginateBy,sort_by);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/all_genres")
    public ResponseEntity<List<Genre>> getAllMoviesGenres(){
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }

    @GetMapping("/popular_movies/page/{page}/pagination/{pagination}")
    public ResponseEntity<MovieListResponse> getPopularMovies(@PathVariable int page, @PathVariable int pagination){

        MovieListResponse response = movieService.getMoviesByPopularity(pagination, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
