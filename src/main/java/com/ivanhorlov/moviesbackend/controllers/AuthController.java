package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.dtos.JwtRequest;
import com.ivanhorlov.moviesbackend.dtos.JwtResponse;
import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import com.ivanhorlov.moviesbackend.exception_handling.MovieIncorrectData;
import com.ivanhorlov.moviesbackend.services.AuthService;
import com.ivanhorlov.moviesbackend.services.UserService;
import com.ivanhorlov.moviesbackend.utils.JwtTokenUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;


//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest){
        String token = authService.createAuthToken(authRequest);
        int id = jwtTokenUtils.getUserId(token);
        String userName = jwtTokenUtils.getUserName(token);
        return ResponseEntity.ok(new JwtResponse(token,userName,id));
    }


    @PostMapping("/registration")
    public ResponseEntity<RegistrationUserDto> userRegistration(@RequestBody RegistrationUserDto registrationUserDto) throws MessagingException, FileNotFoundException, InterruptedException {
        if(authService.userRegistration(registrationUserDto)){
//            return new ResponseEntity<>("Activation code was sent to email: " + registrationUserDto.getEmail(), HttpStatus.OK);
            return new ResponseEntity<>(registrationUserDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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

    @GetMapping("/is_user_exist_with_name/{name}")
    public ResponseEntity<Boolean> isUserExist(@PathVariable String name){
       return new ResponseEntity<>(userService.isUserExist(name), HttpStatus.OK);
    }

    @GetMapping("/is_email_in_use/{email}")
    public ResponseEntity<MovieIncorrectData> isUserEmailAlreadyInUse(@PathVariable String email){
        MovieIncorrectData info = new MovieIncorrectData();
        if(userService.isEmailExist(email)){
            info.setInfo("This email is already using");
            return new ResponseEntity<>(info, HttpStatus.FORBIDDEN);
        }

        info.setInfo("This email doesn't yet using");

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
