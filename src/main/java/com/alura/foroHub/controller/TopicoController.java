package com.alura.foroHub.controller;

import com.alura.foroHub.domain.curso.Curso;
import com.alura.foroHub.domain.curso.CursoRepository;
import com.alura.foroHub.domain.topico.*;
import com.alura.foroHub.domain.usuario.Usuario;
import com.alura.foroHub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
            UriComponentsBuilder uriComponentsBuilder) {

        // Buscar el autor por ID
        Usuario autor = usuarioRepository.findById(datosRegistroTopico.autorId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el curso por ID
        Curso curso = cursoRepository.findById(datosRegistroTopico.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // Crear el nuevo objeto Topico con los objetos autor y curso
        Topico topico = new Topico(
                datosRegistroTopico.titulo(),
                datosRegistroTopico.mensaje(),
                datosRegistroTopico.fechaDeCreacion(),
                autor.getLogin(),  // Guardamos solo el login del autor
                curso.getNombre()  // Guardamos solo el nombre del curso
        );

        // Asignar las relaciones adecuadas
        topico.setAutorEntity(autor);  // Relacionamos el usuario con el tópico
        topico.setCursoEntity(curso);  // Relacionamos el curso con el tópico

        // Guardar el tópico en la base de datos
        topicoRepository.save(topico);

        // Crear el DTO de respuesta con los datos del tópico
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getAutor(),  // Extraemos solo el nombre del autor
                topico.getCurso()   // Extraemos solo el nombre del curso
        );

        // Crear la URI para el nuevo recurso creado
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        // Devolver la respuesta con el código 201 (Creado) y la URI del nuevo tópico
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }


    @GetMapping

    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault(size = 5) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopicos::new));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {

        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topico no encontrado"));


        if (datosActualizarTopico.estado() != null) {
            topico.setEstado(datosActualizarTopico.estado());
        }
        if (datosActualizarTopico.titulo() != null) {
            topico.setTitulo(datosActualizarTopico.titulo());
        }
        if (datosActualizarTopico.mensaje() != null) {
            topico.setMensaje(datosActualizarTopico.mensaje());
        }

        topicoRepository.save(topico);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getAutor(),
                topico.getCurso());

        return ResponseEntity.ok(datosRespuestaTopico);
    }

    @DeleteMapping("/{id}/desactivar")
    @Transactional
    public  ResponseEntity desactivarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.desactivarTopico();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")

    public ResponseEntity retornarDatosTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datostopico = new DatosRespuestaTopico(topico.getId(),topico.getTitulo() , topico.getMensaje(), topico.getFechaDeCreacion(), topico.getAutor(), topico
                .getCurso());
        return  ResponseEntity.ok(datostopico);
    }

}
