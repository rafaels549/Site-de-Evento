package com.example.teste.config;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.teste.models.Eventos;
import com.example.teste.models.QrCode;
import com.example.teste.models.User;
import com.example.teste.repositories.EventosRepository;
import com.example.teste.repositories.QrcodeRepository;
import com.example.teste.repositories.UserRepository;
import com.example.teste.services.TokenService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
@Configuration
public class SecurityFilter extends OncePerRequestFilter {
   
    @Autowired
    TokenService tokenService;

    @Autowired
     UserRepository userRepository;

     @Autowired
      EventosRepository eventosRepository;

      @Autowired
      QrcodeRepository qrcodeRepository;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException{
         var token = this.recoverToken(request);
      try{
         if(token != null){
          
            String email = tokenService.validateToken(token) ;
         
             UserDetails user = userRepository.findByEmail(email);
                     
            
             if (user != null && checkEventOwnership(user,request) && checkQrCodeOwner(user, request)) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                     SecurityContextHolder.getContext().setAuthentication(authentication);
             }

           
                   
         }
         filterChain.doFilter(request, response);
        }catch(Exception e){
                 System.out.println(e.getMessage());
        }
    }


    private String recoverToken(HttpServletRequest request){
                 var authHeader = request.getHeader("Authorization");
                 if(authHeader == null) return null;
                 return authHeader.replace("Bearer", "").trim();

     
                 
             
    }

    private boolean checkEventOwnership(UserDetails user, HttpServletRequest request) {
        if ("/participantes/**".equals(request.getServletPath()) || ("PUT".equals(request.getServletPath())  || "DELETE".equals(request.getServletPath()) &&  "/eventos/**".equals(request.getServletPath())  ))  {
            Map<String, String> pathVariables = new AntPathMatcher().extractUriTemplateVariables(request.getServletPath(), request.getRequestURI());
            String idEvento = pathVariables.get("id");
    
            if (idEvento != null && !idEvento.isEmpty()) {
                Integer id = Integer.parseInt(idEvento);
                Eventos evento = eventosRepository.findById(id).orElse(null);
    
                if (evento != null && evento.getUser().getUsername().equals(user.getUsername())) {
                    return true;
                }
                return false;
            }
        }
    
        return true;
    }

    private boolean checkQrCodeOwner(UserDetails user, HttpServletRequest request) {
        if ("/qrcode/**".equals(request.getServletPath()) ||  "/qrcodes/**".equals(request.getServletPath())  )  {
            Map<String, String> pathVariables = new AntPathMatcher().extractUriTemplateVariables(request.getServletPath(), request.getRequestURI());
            String id = pathVariables.get("id");
    
            if (id != null && !id.isEmpty()) {
                Integer IntegerId = Integer.parseInt(id);
                QrCode qrcode = qrcodeRepository.findById(IntegerId).orElse(null);
                User userGet = userRepository.findById(IntegerId).orElse(null);
                User userPrincipal = (User) user;     
                if (qrcode != null && qrcode.getUser().getEmail().equals(userPrincipal.getEmail()) || (userGet!=null && userGet.getEmail().equals(userPrincipal.getEmail()))) {
                    return true;
                }
                return false;
            }
        }
    
        return true;
    }
    @Bean
    AuthorizationManager<RequestAuthorizationContext> isEventOwner() {
        return new AuthorizationManager<RequestAuthorizationContext>() {
            @Override
            public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

                if ("/participantes/**".equals(context.getRequest().getServletPath())|| ("PUT".equals(context.getRequest().getServletPath())  || "DELETE".equals(context.getRequest().getServletPath()) &&  "/eventos/**".equals(context.getRequest().getServletPath())  )) {
                    String idEvento = context.getVariables().get("id");
                    Integer id = Integer.parseInt(idEvento);
                    
                    Eventos evento = eventosRepository.findById(id).orElse(null);
    
                   
                    UserDetails userDetails = (UserDetails) authentication.get().getPrincipal();
                    User userPrincipal = (User) userDetails;  
                       String email = userPrincipal.getEmail();
                    if (evento != null && evento.getUser().getUsername().equals(email)) {
                        return new AuthorizationDecision(true);
                    } else {
                        return new AuthorizationDecision(false);
                    }
                }else{
                    return new AuthorizationDecision(true);      
                }
                }
            };
        }

        
    @Bean
    public AuthorizationManager<RequestAuthorizationContext> isQRCodeOwner() {
        return (authentication, context) -> {
            if ("/qrcode/**".equals(context.getRequest().getServletPath()) || "/qrcodes/**".equals(context.getRequest().getServletPath() )) {
                String pathId = context.getVariables().get("id");
                Integer intId = Integer.parseInt(pathId);
                QrCode qrcode = qrcodeRepository.findById(intId).orElse(null);
                User userGet = userRepository.findById(intId).orElse(null);
                UserDetails userDetails = (UserDetails) authentication.get().getPrincipal();
                User userPrincipal = (User) userDetails;  

                if (qrcode != null && qrcode.getUser().getEmail().equals(userPrincipal.getEmail()) || (userGet!=null && userGet.getEmail().equals(userPrincipal.getEmail()))) {
                    return new AuthorizationDecision(true);
                }else{
                    return new AuthorizationDecision(false);
                }
            }
            return new AuthorizationDecision(true);
        };
    }
    
    
}
