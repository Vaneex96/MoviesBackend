package com.ivanhorlov.moviesbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;

    public JwtResponse() {
    }
}
