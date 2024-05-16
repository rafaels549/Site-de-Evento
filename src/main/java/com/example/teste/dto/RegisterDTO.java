package com.example.teste.dto;


import com.example.teste.models.UserRole;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(@NotBlank(message = "Email não pode estar em branco") String email ,@NotBlank(message ="Username não pode estar em branco") String username, @NotBlank(message = "password não pode ser nula")String password, @NotBlank(message = "confirmação de senha é obrigatório") String confirmPassord,UserRole role)  {
    public RegisterDTO(String email, String username, String password, String confirmPassord) {
        this(email, username, password, confirmPassord, UserRole.USER); 
    }
}
