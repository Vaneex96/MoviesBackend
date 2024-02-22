package com.ivanhorlov.moviesbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    private String username;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
