package com.example.teste.dto;






import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(@NotBlank(message = "Username não pode estar em branco") String email , @NotBlank(message = "password não pode ser nula")String password)  {
   
}
