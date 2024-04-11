package com.ivanhorlov.moviesbackend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanhorlov.moviesbackend.dtos.MovieListResponse;
import com.ivanhorlov.moviesbackend.dtos.RequestGenresListDto;
import com.ivanhorlov.moviesbackend.entities.Genre;
import com.ivanhorlov.moviesbackend.entities.GenreIdName;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.pagination.SortingTypes;
import com.ivanhorlov.moviesbackend.services.GenreService;
import com.ivanhorlov.moviesbackend.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MoviesControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private MoviesController moviesController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Movie movie;
    private Movie movie2;
    private List<GenreIdName> genreIdNameList;
    private List<Integer> moviesIdsList;

    @BeforeEach
    void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(moviesController).build();
        objectMapper = new ObjectMapper();

        this.genreIdNameList = List.of(new GenreIdName(12, "Adventure"), new GenreIdName(14, "Fantasy"));
        this.moviesIdsList = List.of(11,18,58,98,106,120,121,122,128,254);


        this.movie = new Movie();
        this.movie.setId(106);
        this.movie.setTitle("Predator");
        Date date = new Date(1987,06,12);
        this.movie.setRelease_date(date);
        this.movie.setGenres(this.genreIdNameList);

        this.movie2 = new Movie();
        this.movie2.setId(120);
        this.movie2.setTitle("The Lord of the Rings: The Fellowship of the Ring");
        this.movie2.setGenres(this.genreIdNameList);

    }

    @Test
    void getMovieByIdTest() throws Exception{

        when(movieService.getMovieById(106)).thenReturn(movie);

        mockMvc.perform(get("/movies/movie/id/106"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(movie.getTitle()))
                        .andExpect(jsonPath("$.id").value(movie.getId()))
                        .andExpect(jsonPath("$.release_date").value(movie.getRelease_date()));

        verify(movieService, times(1)).getMovieById(106);
    }

    @Test
    void getMovieByTitle() throws Exception {
        when(movieService.getMovieByTitle("Predator")).thenReturn(movie);

        mockMvc.perform(get("/movies/movie/title/Predator"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.id").value(movie.getId()))
                .andExpect(jsonPath("$.release_date").value(movie.getRelease_date()));

        verify(movieService, times(1)).getMovieByTitle("Predator");
    }

    @Test
    void getMoviesIdsByGenreName() throws Exception {
        when(movieService.getMoviesIdsByGenre("Adventure", 1)).thenReturn(moviesIdsList);

        mockMvc.perform(get("/movies/ids/genrename/Adventure/pagenumber/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$[4]").value(106));

        verify(movieService, times(1)).getMoviesIdsByGenre("Adventure", 1);
    }

    @Test
    void getMoviesIdsByGenreId() throws Exception{
        when(movieService.getMoviesIdsByGenre(12, 1)).thenReturn(moviesIdsList);

        mockMvc.perform(get("/movies/ids/genreid/12/page/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$[4]").value(106));

        verify(movieService, times(1)).getMoviesIdsByGenre(12, 1);
    }

    @Test
    void getMoviesByGenreId() throws Exception{
        RequestGenresListDto requestGenresListDto = new RequestGenresListDto(List.of(12, 14), SortingTypes.popularity_desc);

        MovieListResponse movieListResponse = new MovieListResponse(1, List.of(this.movie, this.movie2));

        String genreIdNameListJson = objectMapper.writeValueAsString(requestGenresListDto);

        when(movieService.getMoviesByGenre(requestGenresListDto, 1, requestGenresListDto.getSortingType(), 12)).thenReturn(movieListResponse);

        mockMvc.perform(post("/movies/by_genres_ids/page/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(genreIdNameListJson))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(status().isOk());


        verify(movieService, times(1)).getMoviesByGenre(requestGenresListDto, 1, requestGenresListDto.getSortingType(), 12);
    }

    @Test
    void getMoviesByTitle() throws Exception{
        MovieListResponse movieListResponse = new MovieListResponse(1, List.of(this.movie));

        when(movieService.getMoviesByTitle("Predator", 1, 12,SortingTypes.popularity_desc)).thenReturn(movieListResponse);

        mockMvc.perform(get("/movies/search_by_title/Predator/page/1/paginate_by/12/sort_by/popularity_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(movieListResponse));

        verify(movieService, times(1)).getMoviesByTitle("Predator", 1, 12,SortingTypes.popularity_desc);

    }

    @Test
    void getAllMoviesGenres() throws Exception {
        List<Genre> genreList = List.of(new Genre(), new Genre());

        when(genreService.getAllGenres()).thenReturn(genreList);

        mockMvc.perform(get("/movies/all_genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    void getPopularMovies() throws Exception {
        MovieListResponse response = new MovieListResponse(1, List.of(this.movie, this.movie2));

        when(movieService.getMoviesByPopularity(12, 1)).thenReturn(response);

        mockMvc.perform(get("/movies/popular_movies/page/1/pagination/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(movieService, times(1)).getMoviesByPopularity(12, 1);
    }

}
