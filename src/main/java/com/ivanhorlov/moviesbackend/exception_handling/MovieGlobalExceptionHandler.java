package com.ivanhorlov.moviesbackend.exception_handling;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MovieGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(MalformedJwtException exception){

        MovieIncorrectData data  = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(NoSuchMovieException exception){

        MovieIncorrectData data  = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(NoSuchElementException exception){

        MovieIncorrectData data  = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler
//    public ResponseEntity<MovieIncorrectData> handleException(Exception exception){
//        MovieIncorrectData data = new MovieIncorrectData();
//        data.setInfo(exception.getMessage());
//
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(UserAlreadyExistsException exception){
        MovieIncorrectData data = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BadCredentialsData> handleException(BadCredentialsException exception){
        BadCredentialsData data = new BadCredentialsData();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(StorageFileNotFoundException exception){
        MovieIncorrectData data = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(HttpMessageNotWritableException exception){
        MovieIncorrectData data = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
