package com.alura.foroHub.domain.usuario;

import com.alura.foroHub.domain.perfil.Perfil;

public record DatosActualizarUsuario(
        Boolean activo,
        String login,
        String email,
        String password,
        Perfil perfil
) {
}
