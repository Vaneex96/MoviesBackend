package com.ivanhorlov.moviesbackend.exception_handling;

import lombok.Data;

import java.util.Date;

@Data
public class BadCredentialsData {
    private String message;
    private Date timestamp;

    public BadCredentialsData(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public BadCredentialsData() {
        this.timestamp = new Date();
    }
}
