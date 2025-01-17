package com.alura.foroHub.controller;


import com.alura.foroHub.domain.perfil.Perfil;
import com.alura.foroHub.domain.perfil.PerfilRepository;
import com.alura.foroHub.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;


    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                                                  UriComponentsBuilder uriComponentsBuilder) {
        Perfil perfilEstandar = perfilRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Perfil estándar no encontrado"));

        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario,perfilEstandar));

        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getDocumento(),
                usuario.getEmail()
        );

        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }


    @GetMapping

    public ResponseEntity<Page<DatosListadoUsuarios>> listadoUsuarios(@PageableDefault(size = 5) Pageable paginacion) {
        return ResponseEntity.ok(usuarioRepository.findByActivoTrue(paginacion).map(DatosListadoUsuarios::new));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public  ResponseEntity eliminarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.desactivarUsuario();
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (datosActualizarUsuario.activo() != null) {
            usuario.setActivo(datosActualizarUsuario.activo());
        }
        if (datosActualizarUsuario.login() != null) {
            usuario.setLogin(datosActualizarUsuario.login());
        }
        if (datosActualizarUsuario.email() != null) {
            usuario.setEmail(datosActualizarUsuario.email());
        }
        if (datosActualizarUsuario.password() != null) {
            usuario.setPassword(datosActualizarUsuario.password());
        }
        if (datosActualizarUsuario.perfil() != null) {
            Perfil perfil = perfilRepository.findById(datosActualizarUsuario.perfil().getId())
                    .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
            usuario.getPerfiles().clear();
            usuario.getPerfiles().add(perfil);
        }

        usuarioRepository.save(usuario);

        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getDocumento(),
                usuario.getEmail()
        );

        return ResponseEntity.ok(datosRespuestaUsuario);
    }

    @GetMapping("/{id}")

    public ResponseEntity retornaDatosUsuario(@PathVariable Long id){
        Usuario usuario = usuarioRepository.getReferenceById(id);
        var datosUsuario = new DatosRespuestaUsuario(usuario.getId(), usuario.getLogin(),usuario.getDocumento(), usuario.getEmail());
        return  ResponseEntity.ok(datosUsuario);
    }



}
