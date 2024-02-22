package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.services.AdditionalUserInfoService;
import com.ivanhorlov.moviesbackend.services.EmailService;
import com.ivanhorlov.moviesbackend.services.MovieService;
import com.ivanhorlov.moviesbackend.services.UserService;
import com.ivanhorlov.moviesbackend.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/id")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final MovieService movieService;
    private final AdditionalUserInfoService additionalUserInfoService;


    @PostMapping("/{id}/change_user_details_info")
    public ResponseEntity<AdditionalUserInfo> changeAdditionalUserInfo(@PathVariable int id,@RequestBody AdditionalUserInfo additionalUserInfo, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

        AdditionalUserInfo changedAdditionalInfo = userService.addAdditionalUserInfo(additionalUserInfo, id);

        return new ResponseEntity<>(changedAdditionalInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}/user_details_info")
    public ResponseEntity<AdditionalUserInfo> getAdditionalUserInfo(@PathVariable int id, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);
        AdditionalUserInfo additionalUserInfo = additionalUserInfoService.getAdditionalUserInfo(id);

        return new ResponseEntity<>(additionalUserInfo, HttpStatus.OK);
    }


    @GetMapping("/{id}/getallinfo")
    public ResponseEntity<User> getAllUserInfo(@PathVariable int id, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

        return new ResponseEntity<>(userService.getUserByUserId(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/add_movie_to_favorite/movie_id/{movieId}")
    public ResponseEntity<String> addMovieToFavorite(@PathVariable int id, @PathVariable int movieId, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

        userService.addMovieToFavorite(movieId, id);

        return new ResponseEntity<>("Movie with id "+movieId+" added to favorite", HttpStatus.OK);
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<String> getEmailByUserId(@PathVariable int id ,HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

         return new ResponseEntity<>(userService.getEmailByUserId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/favorites_movies")
    public ResponseEntity<List<Movie>> getFavoritesMoviesByUserId(@PathVariable int id, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

        return new ResponseEntity<>(movieService.getAllFavoriteMoviesByUserId(id) , HttpStatus.OK);
    }

}
