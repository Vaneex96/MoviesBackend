package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.exception_handling.NoSuchMovieException;
import com.ivanhorlov.moviesbackend.services.MovieService;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MyRestController {

    private final MovieService movieService;

    private final ServletContext servletContext;

    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "Unsecured";
    }

    @GetMapping("/secured")
    public String securedData(){
        return "Secured";
    }

    @GetMapping
    public String adminData(){
        return "Admin data";
    }

    @GetMapping("/moviebyid/id={id}")
    public Movie getMovieById(@PathVariable int id){
        Movie movie = null;
        movie = movieService.getMovieById(id);

        if (movie == null){
            throw new NoSuchMovieException("Movie with id: " + id + " not found");
        }

//        if (movie.getOriginalTitle().equals("Barbie")){
//            movie.setBackdropPath("/img/iravanya.JPG");
//        }

        return movie;
    }

    @GetMapping("/movies/ids/bygenrename/{genreName}")
    public List<Integer> getMoviesIdsByGenreName(@PathVariable String genreName){
        List<Integer> movieList = null;
        movieList = movieService.getMoviesIdsByGenre(genreName, 1);
        if (movieList == null){
            throw new NoSuchElementException();
        }

        return movieList;
    }

    @GetMapping("/movies/ids/bygenreid/{id}/page/{page}")
    public List<Integer> getMoviesIdsByGenreId(@PathVariable int id, @PathVariable int page){
        List<Integer> movieList = null;
        movieList = movieService.getMoviesIdsByGenre(id, page);
        if (movieList == null){
            throw new NoSuchElementException();
        }

        return movieList;
    }

    @GetMapping("/movies/bygenreid/{id}/page/{page}")
    public List<Movie> getMoviesById(@PathVariable int id, @PathVariable int page){
        List<Movie> movieList = new ArrayList<>();
        movieList = movieService.getMoviesByGenre(id, page);
        System.out.println(movieList.toString());
        if(movieList.size() == 0){
            throw new NoSuchElementException();
        }

        return movieList;
    }


    @GetMapping("/img/{img}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String img) throws IOException {
        BufferedImage bImage = ImageIO.read(new File("C:\\Users\\igorl\\OneDrive\\Рабочий стол\\pictures\\" + img));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<byte[]>(data, httpHeaders, HttpStatus.OK);

    }
}
