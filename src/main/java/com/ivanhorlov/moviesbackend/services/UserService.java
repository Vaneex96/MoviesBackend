package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Movie;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.entities.UserMovie;
import com.ivanhorlov.moviesbackend.exception_handling.UserAlreadyExistsException;
import com.ivanhorlov.moviesbackend.repositories.UserMovieRepository;
import com.ivanhorlov.moviesbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MovieService movieService;
    private final UsersFavoriteMoviesService usersFavoriteMoviesService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Optional<User> findByUserName(String username){
        return userRepository.findUserByUserName(username);
    }

    public void createNewUser(User user){
        if(userRepository.findUserByUserName(user.getUserName()).isPresent()){
            throw new UserAlreadyExistsException(String.format("User '%s' already exits", user.getUserName()));
        }

        user.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));
    }


    public User getUserByUserId(int id){
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()){
            throw new NoSuchElementException("User with id: " + id + " not found");
        }

        return user.get();
    }


    public Integer getUserIdByUserName(String username){
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        if(userOptional.isEmpty()){
            throw new NoSuchElementException("User " + username + " not found");
        }
        User user = userOptional.get();
        return user.getId();
    }


    public String getEmailByUserId(int id){
        return getUserByUserId(id).getEmail();
    }

//    public void addMovieToFavorite(int movieId, int userId){
//        UserMovie userMovie = new UserMovie(userId, movieId);
//        usersFavoriteMoviesService.addMovieToFavorite(userMovie);
//    }

    public void addMovieToFavorite(int movieId, int userId) {
        User user = getUserByUserId(userId);
        Movie movie = movieService.getMovieById(movieId);
        Collection<Movie> movieCollection = user.getMovies();

        if(movieCollection.contains(movie)){
            throw new NoSuchElementException("This movie already exits in favorite");
        }

        movieCollection.add(movie);
        user.setMovies(movieCollection);
        userRepository.save(user);

    }

}
