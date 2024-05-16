package com.example.teste.controllers;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.teste.dto.EventoDTO;
import com.example.teste.dto.UserDTO;
import com.example.teste.models.Eventos;
import com.example.teste.models.User;
import com.example.teste.repositories.EventosRepository;
import com.example.teste.services.DiscoService;
import com.example.teste.services.QrCodeGeneratorService;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;





@RestController
public class EventosController {
    
    @Autowired
    EventosRepository eventosRepository;
    
    @Autowired
    private QrCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    private DiscoService disco;
  

    @PostMapping("/eventos")
    public ResponseEntity<String> Criareventos(@RequestBody @Valid Eventos eventos, @RequestParam MultipartFile foto) {


          
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      
           User user = (User) authentication.getPrincipal();

                disco.salvarFoto(foto);
             
            
                 String arquivoName = foto.getOriginalFilename();
                

           Eventos evento = new Eventos(eventos.getName(),eventos.getDate(), eventos.getHorario(), arquivoName, user);

            eventosRepository.save(evento);


            return ResponseEntity.status(HttpStatus.CREATED).body("Evento Criado com sucesso! ");
    }

      @GetMapping("/eventos/{date}")
      public ResponseEntity<?> filtroPorData(@PathVariable(value = "date") Date date) {
               List<Eventos> eventos =  eventosRepository.findByDate(date);
               List<EventoDTO> eventosDTO = new ArrayList<>();
                  for(Eventos evento: eventos){
                               User user = evento.getUser();
                                UserDTO userDTO = new UserDTO(user.getUsername());
                              EventoDTO eventoDTO = new EventoDTO(evento.getName(), evento.getDate(), evento.getHorario(), evento.getImage(), userDTO, evento.getFim());
                               eventosDTO.add(eventoDTO);
                  }
                 if(eventosDTO!=null && eventosDTO.isEmpty()){
                  return ResponseEntity.ok().body(eventosDTO);
                 }else{
                    return ResponseEntity.ok().body("Não há eventos nesta data");     
                 }
                      }
      
    @GetMapping("/eventos")
    public ResponseEntity<?>  eventos() {
              
      

            List<Eventos> eventos = eventosRepository.findAll();
            List<EventoDTO> eventosDTO = new ArrayList<>();
        
            for (Eventos evento : eventos) {
                User user =evento.getUser();
                UserDTO userDTO = new UserDTO(user.getUsername());
               
                EventoDTO eventoDTO = new EventoDTO(evento.getName(),evento.getDate(),evento.getHorario(), evento.getImage(),userDTO, evento.getFim());
             
              
                eventosDTO.add(eventoDTO);
            }
            return  ResponseEntity.status(HttpStatus.OK).body(eventosDTO);
    }
    
    
    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoDTO> evento(@PathVariable(value = "id") Integer idEvento) {
              var eventoRepository = eventosRepository.findById(idEvento);
           
              if(eventoRepository.isPresent()){
                            Eventos evento = eventoRepository.get();
                            User user =evento.getUser();
                            UserDTO userDTO = new UserDTO(user.getUsername());
               
                                EventoDTO eventoDTO = new EventoDTO(evento.getName(),evento.getDate(),evento.getHorario(), evento.getImage(),userDTO,evento.getFim());
                            
                           

                            return ResponseEntity.status(HttpStatus.OK).body(eventoDTO);
              }
              else{
                                 return ResponseEntity.notFound().build();
              }

        
    }


    @GetMapping("/eventos/{id}/participantes")
    @PreAuthorize("@customAuthManager.check(authentication, #id)")
    public ResponseEntity<Object> participantesEvento(@PathVariable(value = "id") Integer idEvento) {
              var eventoRepository = eventosRepository.findById(idEvento);
              UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
              String username = userDetails.getUsername();
              if(eventoRepository.get().getUser().getUsername().equals(username)){
                            Eventos evento = eventoRepository.get();
                             List<UserDTO> usersDTO = new ArrayList<>();
                            List <User> users = evento.getUsers();
                             for (User  user: users) {
                                UserDTO userDTO = new UserDTO(user.getUsername());
                                  usersDTO.add(userDTO);
                             }
                           
                          

                            return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
              }else{
                return ResponseEntity.status(HttpStatus.LOCKED).body("Você não tem acesso a página"); 
              }
             

        
    }

    @PostMapping("/eventos/{id}/participar")
    public ResponseEntity<Object> participar(@PathVariable(value = "id") Integer idEvento) {
              var eventoRepository = eventosRepository.findById(idEvento);

              if(eventoRepository.isPresent()){
                            Eventos evento = eventoRepository.get();
                            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      
                            User user = (User) authentication.getPrincipal();
                            
                            evento.getUsers().add(user);

                            eventosRepository.save(evento);

                            try {
                                MultipartFile qrCodeImage = qrCodeGeneratorService.generateQrCodeImage(user.getUsername(), 200, 200);
                                 disco.salvarQr(qrCodeImage);

                                 return ResponseEntity.ok().body("Parabéns ! Você está participando do evento");
                                   
                            } catch (IOException e) {
                                return ResponseEntity.badRequest().build();
                            }
                             
                            

                          
              }
              else{
                                 return ResponseEntity.notFound().build();
              }

        
    }


    



   
    @PutMapping("eventos/{id}")
    public ResponseEntity<String> atualizar(@PathVariable(name = "id") Integer idEvento, @RequestBody @Valid Eventos eventos) {
        var    existingEvento = eventosRepository.findById(idEvento);

         
        if(existingEvento.isPresent()){
            Eventos evento = existingEvento.get();

                  evento.setName(eventos.getName());
                  evento.setImage(eventos.getImage());
                 

                  eventosRepository.save(evento);
            return ResponseEntity.ok().body("Evento Atualizado com sucesso");
}
else{
                 return ResponseEntity.notFound().build();
}
        
       
    }
   
        
    
    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<String> deletarEvento(@PathVariable(value = "id") Integer idEvento) {
      
        if (eventosRepository.existsById(idEvento)) {
          
            eventosRepository.deleteById(idEvento);
            return ResponseEntity.ok().body("Evento deletado com sucesso");
        } else {
            
            return ResponseEntity.notFound().build();
        }
    }

   
}
    


