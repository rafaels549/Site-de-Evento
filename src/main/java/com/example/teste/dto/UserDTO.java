package com.example.teste.dto;

import com.example.teste.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;

    public UserDTO() {
    }

    public UserDTO(String username) {
        this.username = username;
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
       
        return user;
    }

}
