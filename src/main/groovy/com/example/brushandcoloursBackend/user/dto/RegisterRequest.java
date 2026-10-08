package com.example.brushandcoloursBackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nams is Required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email mus be Valid")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password must contain at-least 8 characters")
    private String password;
}
