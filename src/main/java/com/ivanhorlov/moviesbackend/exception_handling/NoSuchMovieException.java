package com.ivanhorlov.moviesbackend.exception_handling;


public class NoSuchMovieException extends RuntimeException {

    public NoSuchMovieException(String message) {
        super(message);
    }
}
