package com.alura.foroHub.domain.usuario;

public record DatosListadoUsuarios(
        Long id,
        String login,
        String documento,
        String email,
        Boolean estado
) {
    public DatosListadoUsuarios(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getDocumento(),usuario.getEmail(),usuario.getActivo());
    }
}
