package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.dtos.JwtRequest;
import com.ivanhorlov.moviesbackend.dtos.JwtResponse;
import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import com.ivanhorlov.moviesbackend.services.AuthService;
import com.ivanhorlov.moviesbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest){
        return ResponseEntity.ok(new JwtResponse(authService.createAuthToken(authRequest)));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> userRegistration(@RequestBody RegistrationUserDto registrationUserDto){
        if(authService.userRegistration(registrationUserDto)){
            return new ResponseEntity<>("Activation code was sent to email: " + registrationUserDto.getEmail(), HttpStatus.OK);
        }

        return new ResponseEntity<>("User with name: " + registrationUserDto.getPassword() + " already exists", HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/account_activation/{code}")
    public ResponseEntity<String> activateAccount(@PathVariable String code){
        if (userService.isActivationCode(code)){
            return new ResponseEntity<>("Activation completed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Account has already been activated", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
        if(authService.resetPassword(resetPasswordDto)){
            return new ResponseEntity<>("New password was sent to" + resetPasswordDto.getEmail(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Incorrect email or user with email " + resetPasswordDto.getEmail() + " doesn't exist", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordDto resetPasswordDto){
        if(authService.changePassword(resetPasswordDto)){
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

}
