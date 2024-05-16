package com.example.teste.controllers;

import org.springframework.web.bind.annotation.RestController;


import com.example.teste.dto.UserInfoDTO;
import com.example.teste.models.User;
import com.example.teste.repositories.UserRepository;
import com.example.teste.services.TokenService;



import org.springframework.web.bind.annotation.RequestMapping;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;







@RestController
@RequestMapping("user")
public class UserInfo {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenService tokenService;


    @GetMapping("/info")
    public ResponseEntity<?> getUser(@RequestBody String token) {
               String email = tokenService.validateToken(token);
               
               UserDetails user = userRepository.findByEmail(email);
                User userPrincipal = (User) user;
                UserInfoDTO userDTO = new UserInfoDTO( userPrincipal.getEmail(),userPrincipal.getUsername());




              return ResponseEntity.ok().body(userDTO);
                

    }
    @GetMapping("/eventos")
    public ResponseEntity<?> getEventos(@RequestBody String token) {
               String email = tokenService.validateToken(token);
               
               UserDetails user = userRepository.findByEmail(email);
                User userPrincipal = (User) user;
                UserInfoDTO userDTO = new UserInfoDTO(userPrincipal.getEventosOwned(),userPrincipal.getEventos());




              return ResponseEntity.ok().body(userDTO);
                

    }
    @GetMapping("/qrcodes")
    public ResponseEntity<?> getQrcode(@RequestBody String token) {
               String email = tokenService.validateToken(token);
               
               UserDetails user = userRepository.findByEmail(email);
                User userPrincipal = (User) user;
                UserInfoDTO userDTO = new UserInfoDTO(userPrincipal.getQrcodes());


                   

              return ResponseEntity.ok().body(userDTO);
                

    }


    
}
