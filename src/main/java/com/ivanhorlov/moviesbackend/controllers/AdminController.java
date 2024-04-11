package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/info")
    public String adminData(){
        return "Admin data";
    }

    @GetMapping("/delete_unverified_users")
    public ResponseEntity<String> deleteUnverifiedUsers(){
        userService.deleteUserWithoutConfirmEmail();
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }

}
