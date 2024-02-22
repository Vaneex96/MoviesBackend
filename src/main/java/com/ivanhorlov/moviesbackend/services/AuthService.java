package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.JwtRequest;
import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;

public interface AuthService {
    String createAuthToken(JwtRequest authRequest);
    boolean userRegistration(RegistrationUserDto registrationUserDto);
    boolean resetPassword(ResetPasswordDto resetPasswordDto);

    boolean changePassword(ResetPasswordDto resetPasswordDto);
}
