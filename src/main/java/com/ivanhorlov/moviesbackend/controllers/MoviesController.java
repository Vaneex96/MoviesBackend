package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class MoviesController {

    private final MovieService movieService;


    @GetMapping("/movie/id/{id}")
    public Movie getMovieById(@PathVariable int id){
        return movieService.getMovieById(id);
    }

    @GetMapping("/movie/title/{title}")
    public Movie getMovieByTitle(@PathVariable String title){
        return movieService.getMovieByTitle(title);
    }

    @GetMapping("/movies/ids/genrename/{genreName}/pagenumber/{pagenumber}")
    public List<Integer> getMoviesIdsByGenreName(@PathVariable String genreName, @PathVariable int pagenumber){
        return movieService.getMoviesIdsByGenre(genreName, pagenumber);
    }

    @GetMapping("/movies/ids/genreid/{id}/page/{page}")
    public List<Integer> getMoviesIdsByGenreId(@PathVariable int id, @PathVariable int page){
        return movieService.getMoviesIdsByGenre(id, page);
    }

    @GetMapping("/movies/genreid/{id}/page/{page}")
    public List<Movie> getMoviesByGenreId(@PathVariable int id, @PathVariable int page){
        return movieService.getMoviesByGenre(id, page);
    }

}
