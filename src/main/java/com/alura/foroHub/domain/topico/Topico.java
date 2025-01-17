package com.alura.foroHub.domain.topico;

import com.alura.foroHub.domain.curso.Curso;
import com.alura.foroHub.domain.respuesta.Respuesta;
import com.alura.foroHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Table(name = "topicos")
@Entity(name = "Topico")

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(nullable = false, unique = true)
    private String mensaje;

    @Column(name = "fecha_de_creacion", nullable = false)
    private LocalDateTime fechaDeCreacion;

    @Column(nullable = false)
    private boolean estado;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String curso;

    // Relación uno a muchos con Respuestas (un tópico puede tener varias respuestas)
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL)
    private List<Respuesta> respuestas = new ArrayList<>();

    // Relación muchos a uno con Curso (con la entidad Curso)
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso cursoEntity;

    // Relación muchos a uno con Usuario (con la entidad Usuario)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autorEntity;

    public Topico(String titulo, String mensaje, LocalDateTime fechaDeCreacion, String autor, String curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaDeCreacion = fechaDeCreacion;
        this.estado = true;
        this.autor = autor;
        this.curso = curso;
    }


    public void desactivarTopico() {
        this.estado= false;
    }
}


