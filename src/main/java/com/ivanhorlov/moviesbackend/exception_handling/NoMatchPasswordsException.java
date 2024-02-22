package com.ivanhorlov.moviesbackend.exception_handling;

public class NoMatchPasswordsException extends RuntimeException{
    public NoMatchPasswordsException(String message) {
        super(message);
    }
}
