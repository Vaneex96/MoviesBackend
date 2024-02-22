package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import com.ivanhorlov.moviesbackend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    public void sendMail(String receiver, String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Текстовое письмо");
        message.setText(text);

        javaMailSender.send(message);
    }



    @Override
    public void sendActivationCode(RegistrationUserDto registrationUserDto, String activationCode) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(registrationUserDto.getEmail());
        message.setSubject("Text message");

        String text = String.format("Hello, %s \n" +
                                        "Welcome to Movies-rezka. Please visit the link to activate your account: " +
                "http://localhost:8080/account_activation/%s",registrationUserDto.getUsername(), activationCode);

        message.setText(text);

        javaMailSender.send(message);
    }


    @Override
    public boolean sendEmailToResetPassword(ResetPasswordDto resetPasswordDto){
        User user = userService.getUserByEmail(resetPasswordDto.getEmail());

        if(user == null){
//            throw new NoSuchElementException(String.format("User with email %s doesn't exist", resetPasswordDto.getEmail()));
            return false;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(resetPasswordDto.getEmail());
        message.setSubject("Text message");

        String newPassword = UUID.randomUUID().toString();
        String encodedPassword = encoder.encode(newPassword);
        message.setText("Your new password: " + newPassword);

        user.setPassword(encodedPassword);
        userService.saveUser(user);

        javaMailSender.send(message);

        return true;
    }

}
