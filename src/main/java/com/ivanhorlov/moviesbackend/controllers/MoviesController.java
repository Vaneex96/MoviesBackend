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
    public ResponseEntity<Movie> getMovieById(@PathVariable int id){
        return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping("/movie/title/{title}")
    public ResponseEntity<Movie> getMovieByTitle(@PathVariable String title){
        return new ResponseEntity<>(movieService.getMovieByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/ids/genrename/{genreName}/pagenumber/{pagenumber}")
    public ResponseEntity<List<Integer>> getMoviesIdsByGenreName(@PathVariable String genreName, @PathVariable int pagenumber){
        return new ResponseEntity<>(movieService.getMoviesIdsByGenre(genreName, pagenumber), HttpStatus.OK);
    }

    @GetMapping("/ids/genreid/{id}/page/{page}")
    public ResponseEntity<List<Integer>> getMoviesIdsByGenreId(@PathVariable int id, @PathVariable int page){
        return new ResponseEntity<>(movieService.getMoviesIdsByGenre(id, page), HttpStatus.OK);
    }

    @PostMapping("/by_genres_ids/page/{page}")
    public ResponseEntity<MovieListResponse> getMoviesByGenreId(@RequestBody RequestGenresListDto genres, @PathVariable int page){
        return new ResponseEntity<>(movieService.getMoviesByGenre(genres, page, genres.getSortingType(), 12), HttpStatus.OK);
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
