package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;

public interface EmailService {
    void sendMail(String receiver, String text);
    boolean sendActivationCode(RegistrationUserDto registrationUserDto, String activationCode) throws MessagingException, FileNotFoundException;

    boolean sendEmailToResetPassword(ResetPasswordDto resetPasswordDto);
}
