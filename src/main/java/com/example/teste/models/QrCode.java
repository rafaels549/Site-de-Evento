package com.example.teste.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="qrcode")
public class QrCode {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id; 

      private String nome;
      
     @ManyToOne(cascade = CascadeType.ALL)
     private User user;
}
