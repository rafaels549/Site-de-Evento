package com.example.teste.config;

import java.io.IOException;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
         String authHeader = request.getHeader("Authorization");
         String invalidAuthHeader = "Bearer invalid-token";
         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
           SecurityContextHolder.clearContext();
        response.setHeader("Authorization", invalidAuthHeader);
               response.setStatus(HttpServletResponse.SC_OK);
    try {
        response.getWriter().write("Logout feito com sucesso");
    } catch (IOException e) {
      
        e.printStackTrace();
    }
              
       
    }
            
}
