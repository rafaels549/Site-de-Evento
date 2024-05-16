package com.example.teste.repositories;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.teste.models.User;



@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

         UserDetails findByUsername(String username);

         UserDetails findByEmail(String email);
    
} 
