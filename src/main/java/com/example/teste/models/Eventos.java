package com.example.teste.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;
import jakarta.persistence.GeneratedValue;


import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;



@Entity
@Table(name = "TB_EVENTOS")
@AllArgsConstructor
@Getter
@Setter
public class Eventos implements Serializable{
      private static final long serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy=GenerationType.AUTO)
      private Integer idEvento;

     @Column(nullable = false , length = 20)
     @NotBlank(message = "Nome não pode estar em branco")
      private String name;

      @Column(nullable = false )
      @NotBlank(message = "Imagem não pode estar em branco")
      private String image;


      @Column(nullable = false)
      @NotBlank(message = "Data não pode estar em branco")
      private Date date;

      @Column(nullable=false)
      @NotBlank(message = "Horário não pode estar em branco")
      private Time  horario; 

      @Column(nullable=false)
      @NotBlank(message = "Horário não pode estar em branco")
        private Time fim;

         @ManyToOne(cascade = CascadeType.ALL)
         @JoinColumn(name = "user_id")
         @OnDelete(action = OnDeleteAction.CASCADE)
         private User user;


      @ManyToMany(mappedBy = "eventos",cascade = CascadeType.ALL)
      @OnDelete(action = OnDeleteAction.CASCADE)
      private  List<User> users = new ArrayList<>();
 
      
    public Eventos(){
             
    }


    public Eventos(String name,Date date, Time  horario, String image,User user) {
        this.name = name;
        this.image = image;
        this.user = user;
        this.date = date;
        this.horario = horario;
    }


}
