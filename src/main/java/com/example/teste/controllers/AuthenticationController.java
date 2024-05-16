package com.example.teste.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.teste.dto.AuthenticationDTO;
import com.example.teste.dto.RegisterDTO;
import com.example.teste.models.User;
import com.example.teste.repositories.UserRepository;
import com.example.teste.services.TokenService;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenService tokenService;
    
  

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data, HttpServletRequest request )  {
      
        var emailPassword = new UsernamePasswordAuthenticationToken(data.email(),data.password());
     
        var auth = this.authenticationManager.authenticate(emailPassword);
        var token  = tokenService.generateToken((User)auth.getPrincipal());
   
        return ResponseEntity.ok().body(token);
      
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) throws Exception{

        if (!data.password().equals(data.confirmPassord())) {
            throw new Exception("A senha e a confirmação de senha não coincidem.");
        }
           
               
        
            RegisterDTO register = new RegisterDTO(data.email(),data.username(),data.password(),data.confirmPassord());
            String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());
        User newUser = new User(register.username(),register.email(), encryptedPassword, register.role());

        userRepository.save(newUser);


        return ResponseEntity.ok().build();
    }
    

    
    
}
