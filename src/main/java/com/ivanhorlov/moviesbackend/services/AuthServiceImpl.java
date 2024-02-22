package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.JwtRequest;
import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import com.ivanhorlov.moviesbackend.entities.User;
import com.ivanhorlov.moviesbackend.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder;

    @Override
    public String createAuthToken(JwtRequest authRequest) {
        if(!authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())).isAuthenticated()){
            throw new BadCredentialsException("Incorrect login or password");
        }

        if(userService.findUserByUserName(authRequest.getUsername()).getActivationCode() != null){
            throw new BadCredentialsException("Incorrect login or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return token;
    }

    @Override
    public boolean userRegistration(RegistrationUserDto registrationUserDto) {
        if(userService.addUser(registrationUserDto) != null){
            String activationCode = userService.findUserByUserName(registrationUserDto.getUsername()).getActivationCode();
            emailService.sendActivationCode(registrationUserDto, activationCode);
            return true;
        }

        return false;
    }

    @Override
    public boolean resetPassword(ResetPasswordDto resetPasswordDto) {
        if(emailService.sendEmailToResetPassword(resetPasswordDto)){
            return true;
        }

        return false;
    }

    @Override
    public boolean changePassword(ResetPasswordDto resetPasswordDto) {
        JwtRequest jwtRequest = new JwtRequest(resetPasswordDto.getUsername(), resetPasswordDto.getCurrentPassword());
        String token = null;
        token = createAuthToken(jwtRequest);
        User user = new User();

        if(token != null){
            user = userService.findUserByUserName(resetPasswordDto.getUsername());
        } else {
            return false;
        }

        user.setPassword(encoder.encode(resetPasswordDto.getConfirmPassword()));
        userService.saveUser(user);

        return true;
    }
}


