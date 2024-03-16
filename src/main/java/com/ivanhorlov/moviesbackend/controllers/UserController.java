package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.entities.AdditionalUserInfo;
import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.services.AdditionalUserInfoService;
import com.ivanhorlov.moviesbackend.services.MovieService;
import com.ivanhorlov.moviesbackend.services.UserService;
import com.ivanhorlov.moviesbackend.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/id")
@RequiredArgsConstructor
public class UserController {

    @Value("${server-address.url}")
    private String serverAddress;

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final MovieService movieService;
    private final AdditionalUserInfoService additionalUserInfoService;


    @RequestMapping(path = "/{id}/upload_avatar", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> saveAvatar(@PathVariable int id, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        jwtTokenUtils.comparePathIdAndTokenId(id, request);
        String fileName = userService.saveAvatar(file, id);

        if (fileName == null){
            throw new FileUploadException("You are trying to upload a file that is empty or is the wrong size or type");
        }

        return new ResponseEntity<>(serverAddress + "/photos/" + fileName, HttpStatus.OK);
    }


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

    @GetMapping("/{id}/check_movie_in_favorite/movie_id/{movieId}")
    public ResponseEntity<Boolean> isMovieInFavorite(@PathVariable int id, @PathVariable int movieId){

        Boolean response = userService.isMovieInFavorite(id, movieId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}/email")
    public ResponseEntity<String> getEmailByUserId(@PathVariable int id ,HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

         return new ResponseEntity<>(userService.getEmailByUserId(id), HttpStatus.OK);
    }


//    @GetMapping("/{id}/favorites_movies")

    @RequestMapping(value="/{id}/favorites_movies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Movie>> getFavoritesMoviesByUserId(@PathVariable int id, HttpServletRequest request){
        jwtTokenUtils.comparePathIdAndTokenId(id, request);

        return new ResponseEntity<>((List<Movie>) userService.getUserByUserId(id).getMovies() , HttpStatus.OK);
    }

}
