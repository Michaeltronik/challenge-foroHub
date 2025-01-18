package com.alura.foroHub.service;


import com.alura.foroHub.domain.ValidationException;
import com.alura.foroHub.domain.curso.Curso;
import com.alura.foroHub.domain.curso.CursoRepository;
import com.alura.foroHub.domain.topico.*;
import com.alura.foroHub.domain.usuario.Usuario;
import com.alura.foroHub.domain.usuario.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }


    public ResponseEntity<DatosRespuestaConfirmacion> registrarTopico(
            DatosRegistroTopico datosRegistroTopico,
            UriComponentsBuilder uriComponentsBuilder) {

        // Buscar el autor por ID
        Usuario autor = usuarioRepository.findById(datosRegistroTopico.autorId())
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        // Buscar el curso por ID
        Curso curso = cursoRepository.findById(datosRegistroTopico.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Crear el nuevo objeto Topico con los objetos autor y curso
        Topico topico = new Topico(
                datosRegistroTopico.titulo(),
                datosRegistroTopico.mensaje(),
                datosRegistroTopico.fechaDeCreacion(),
                autor.getLogin(),
                curso.getNombre()
        );

        topico.setAutorEntity(autor);  // Relacionamos el usuario con el tópico
        topico.setCursoEntity(curso);  // Relacionamos el curso con el tópico

        // Guardar el tópico en la base de datos
        topicoRepository.save(topico);

        // Preparar los datos de respuesta
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getAutor(),
                topico.getCurso()
        );

        // Crear la URI de la nueva entrada
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        // Preparar la respuesta de confirmación
        DatosRespuestaConfirmacion respuestaConfirmacion = new DatosRespuestaConfirmacion(
                "Registro creado con éxito",
                datosRespuestaTopico
        );

        // Retornar la respuesta con estado 201 (CREATED)
        return ResponseEntity.created(url).body(respuestaConfirmacion);
    }


    //Solicitud Listar topicos

    public Page<DatosListadoTopicos> obtenerListadoTopicos(Pageable paginacion) {
        Page<Topico> topicos = topicoRepository.findAll(paginacion);
        return topicos.map(DatosListadoTopicos::new);
    }

    // Solicitud Actualizar topico

    public DatosRespuestaActualizacion actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico) {
        String mensaje;
        Optional<Topico> optionalTopico = topicoRepository.findById(id);

        // Verificar si el tópico existe
        if (optionalTopico.isEmpty()) {
            throw new ValidationException("Tópico no encontrado");
        }

        Topico topico = optionalTopico.get(); // Obtener el tópico

        // Inicializar un indicador para detectar cambios
        boolean actualizado = false;

        // Verificar y actualizar solo si el nuevo valor es diferente
        if (datosActualizarTopico.estado() != null && !datosActualizarTopico.estado().equals(topico.isEstado())) {
            topico.setEstado(datosActualizarTopico.estado());
            actualizado = true;
        }
        if (datosActualizarTopico.titulo() != null && !datosActualizarTopico.titulo().equals(topico.getTitulo())) {
            topico.setTitulo(datosActualizarTopico.titulo());
            actualizado = true;
        }
        if (datosActualizarTopico.mensaje() != null && !datosActualizarTopico.mensaje().equals(topico.getMensaje())) {
            topico.setMensaje(datosActualizarTopico.mensaje());
            actualizado = true;
        }

        // Si no se realizaron cambios, establecer el mensaje correspondiente
        if (!actualizado) {
            mensaje = "No se realizaron cambios, los datos ya están actualizados.";
        } else {
            // Guardar solo si hubo cambios
            topicoRepository.save(topico);
            mensaje = "Registro actualizado con éxito.";
        }

        // Crear la respuesta de actualización
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getAutor(),
                topico.getCurso()
        );

        return new DatosRespuestaActualizacion(mensaje, datosRespuestaTopico);
    }

//Solicitud de borrar (Delete Lógico) un topico especifico por ID

    public void desactivarTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        topico.desactivarTopico(); // Lógica de negocio para desactivar
    }

//Solicitud de borrar (Delete completo a nivel de base de Datos) un topico por ID

    public void eliminarTopico(Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado");
        }
        topicoRepository.deleteById(id); // Lógica de negocio para eliminar
    }

    //Solicitud de obtener un topico especifico por ID

    public ResponseEntity<?> obtenerTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getAutor(),
                topico.getCurso()
        );

        return ResponseEntity.ok(datosRespuestaTopico);
    }
}

