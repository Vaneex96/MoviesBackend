package com.ivanhorlov.moviesbackend.configs;

import com.ivanhorlov.moviesbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return
                http
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers( "/admin/**").hasRole("ADMIN")
//                                .requestMatchers( "/{username}").hasRole("USER")
                                .requestMatchers("/secured").hasAnyRole("ADMIN","USER")
                                .requestMatchers("/info").authenticated()
                                .anyRequest().permitAll())
                        .httpBasic()
                        .and()
                        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }

}