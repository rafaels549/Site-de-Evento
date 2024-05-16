package com.example.teste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.teste.models.QrCode;


@Repository
public interface QrcodeRepository extends  JpaRepository<QrCode,Integer>  {
       
}
