package com.ivanhorlov.moviesbackend.exception_handling;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
