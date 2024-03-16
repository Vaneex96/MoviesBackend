package com.ivanhorlov.moviesbackend.dtos;

import com.ivanhorlov.moviesbackend.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieListResponse {
    private int total_pages;
    private List<Movie> movie_list;
}
