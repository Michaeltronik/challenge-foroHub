package com.alura.foroHub.domain.usuario;


import com.alura.foroHub.domain.perfil.Perfil;
import com.alura.foroHub.domain.respuesta.Respuesta;
import com.alura.foroHub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private Boolean activo;

    @NotBlank
    @Column(nullable = false)
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_perfil",  // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"),  // Columna para la entidad controladora
            inverseJoinColumns = @JoinColumn(name = "perfil_id")  // Columna para la entidad inversa
    )
    private List<Perfil> perfiles = new ArrayList<>();


    @OneToMany(mappedBy = "autor",fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    @OneToMany(mappedBy = "autor",fetch = FetchType.LAZY)
    private List<Topico> topicos= new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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


    public Usuario(DatosRegistroUsuario datosRegistroUsuario, Perfil perfilEstandar) {
        this.activo = true;
        this.login = datosRegistroUsuario.login();
        this.documento = datosRegistroUsuario.documento();
        this.email = datosRegistroUsuario.email();
        this.password = datosRegistroUsuario.password();
        this.perfiles.add(perfilEstandar);
    }



    public void desactivarUsuario() {
        this.activo=false;
    }


}

