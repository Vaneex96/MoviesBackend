package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;

public interface EmailService {
    void sendMail(String receiver, String text);
    void sendActivationCode(RegistrationUserDto registrationUserDto, String activationCode);

    boolean sendEmailToResetPassword(ResetPasswordDto resetPasswordDto);
}
