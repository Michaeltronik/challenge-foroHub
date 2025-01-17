package com.alura.foroHub.domain.perfil;

import com.alura.foroHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Table(name = "perfiles")
@Entity(name = "Perfil")

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "perfiles", cascade = CascadeType.ALL)
    private List<Usuario> usuarios = new ArrayList<>();


}

