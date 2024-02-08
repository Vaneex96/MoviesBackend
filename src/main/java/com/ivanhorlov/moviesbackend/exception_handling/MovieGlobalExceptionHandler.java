package com.ivanhorlov.moviesbackend.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MovieGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(NoSuchMovieException exception){
        MovieIncorrectData data  = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MovieIncorrectData> handleException(Exception exception){
        MovieIncorrectData data = new MovieIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
