package com.example.teste.models;

import java.util.Collection;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;

@Table(name="users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
     

    @Column(nullable = false )
    @NotNull(message = "O nome de usuário é obrigatório")
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    private String username;

    @Column(nullable = false)
    @NotNull(message = "A senha é obrigatório")
    @NotBlank(message = "O Sennha não pode estar em branco")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String password;
    

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false)
    private UserRole role;
       
     @OneToMany(mappedBy = "user")
     List<QrCode> qrcodes = new ArrayList<>();
    

     @OneToMany(mappedBy = "user")
      @JsonIgnore
      @OnDelete(action = OnDeleteAction.CASCADE)
     private List<Eventos> eventosOwned = new ArrayList<>();
      
     @ManyToMany()
     @JoinTable(
         name = "user_eventos",
         joinColumns = @JoinColumn(name = "user_id"),
         inverseJoinColumns = @JoinColumn(name ="evento_id")

     )
     private List<Eventos> eventos = new ArrayList<>();


     

     public User(String username, String email, String password, UserRole role ){
                this.username = username;
                this.password = password;
                this.email = email;
                this.role = role;
     }

     public User(String username){
       
        this.username = username;
     }

  
    @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER") );
        else return List.of(new SimpleGrantedAuthority("ROLE_USER") );
    }
     
   
    @Override
	public String getUsername() {
		return this.username;
	}
   
    @Override
    public boolean isAccountNonExpired() {
       return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
       return true;
    }
  
    @Override
    public boolean isEnabled() {
         return true;
    }


    
}
