package com.ivanhorlov.moviesbackend.repositories;

import com.ivanhorlov.moviesbackend.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findUserByUserName(String username);
    Optional<User> findUserById(int id);
    Optional<User> findUserByActivationCode(String code);
    Optional<User> findUserByEmail(String email);

}
