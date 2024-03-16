package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.JwtRequest;
import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;

public interface AuthService {
    String createAuthToken(JwtRequest authRequest);
    boolean userRegistration(RegistrationUserDto registrationUserDto) throws MessagingException, FileNotFoundException, InterruptedException;
    boolean resetPassword(ResetPasswordDto resetPasswordDto);

    boolean changePassword(ResetPasswordDto resetPasswordDto);
}
