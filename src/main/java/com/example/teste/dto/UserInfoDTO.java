package com.example.teste.dto;

import  java.util.List;

import com.example.teste.models.Eventos;
import com.example.teste.models.QrCode;

import jakarta.validation.constraints.NotBlank;

public record UserInfoDTO(@NotBlank String email, @NotBlank String username , List<QrCode> qrcodes, List<Eventos> eventosOwned, List<Eventos> eventos )  {
          public UserInfoDTO(String email, String username){
                    this(email, username, null,null,null);
          }


          public UserInfoDTO( List<Eventos> eventosOwned, List<Eventos> eventos) {
            this(null, null, null, eventosOwned, eventos);
        }

        
        public UserInfoDTO(List<QrCode> qrcodes) {
            this(null, null, qrcodes, null, null);
        }
}
