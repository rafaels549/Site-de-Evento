package com.example.teste.dto;

import java.sql.Date;
import java.sql.Time;




import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventoDTO {
      

      @NotNull
      private String name;

      @NotNull
      private String image;


      @NotNull
      private Date date;

      @NotNull
      private Time  horario; 

      @NotNull
       private UserDTO user;

       @NotNull
       private Time fim;


       public EventoDTO(){
         
       }

       
    public EventoDTO(String name,Date date, Time  horario, String image,UserDTO user, Time fim) {
      this.name = name;
      this.image = image;
      this.user = user;
      this.date = date;
      this.horario = horario;
      this.fim =  fim ;
  }

}
