package com.alura.foroHub.controller;
import com.alura.foroHub.domain.curso.CursoRepository;
import com.alura.foroHub.domain.topico.*;
import com.alura.foroHub.domain.usuario.UsuarioRepository;
import com.alura.foroHub.service.TopicoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private final TopicoService topicoService;

    @Autowired
    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    // Create

    @PostMapping
    public ResponseEntity<DatosRespuestaConfirmacion> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
            UriComponentsBuilder uriComponentsBuilder) {
        return topicoService.registrarTopico(datosRegistroTopico, uriComponentsBuilder);
    }

    //Read

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault(size = 5, sort = "fechaDeCreacion") Pageable paginacion) {
        Page<DatosListadoTopicos> datosListadoTopicos = topicoService.obtenerListadoTopicos(paginacion);
        return ResponseEntity.ok(datosListadoTopicos);
    }


    //Update

    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaActualizacion> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {

        // Llamada al servicio para actualizar el tópico
        DatosRespuestaActualizacion respuestaActualizacion = topicoService.actualizarTopico(id, datosActualizarTopico);

        // Retornar la respuesta de la actualización si se realiza
        return ResponseEntity.ok(respuestaActualizacion);
    }

    //Delete
    //Eliminacion lógica
    @DeleteMapping("/{id}/desactivar")
    @Transactional
    public ResponseEntity<Void> desactivarTopico(@PathVariable Long id) {
        topicoService.desactivarTopico(id);
        return ResponseEntity.noContent().build();
    }

    //Eliminacion completa a nivel de base de datos
    @DeleteMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTopico(@PathVariable Long id) {
        return topicoService.obtenerTopico(id);
    }


}
