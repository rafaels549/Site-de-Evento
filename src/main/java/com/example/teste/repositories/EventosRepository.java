package com.example.teste.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.teste.models.Eventos;

@Repository
public interface EventosRepository extends JpaRepository<Eventos,Integer > {
        List<Eventos>  findByDate(Date date);
}
