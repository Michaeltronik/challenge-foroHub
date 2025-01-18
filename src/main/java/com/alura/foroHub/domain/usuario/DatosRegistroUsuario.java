package com.alura.foroHub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroUsuario(

        @NotBlank(message = "login es un campo obligatorio")
        String login,

        @NotBlank
        @Pattern(regexp = "\\d{8,12}", message = "El documento debe tener entre 8 y 12 dígitos")
        String documento,
        @NotBlank(message = "Email es un campo obligatorio")
        @Email
        String email,
        @NotBlank(message = "password es un campo obligatorio")
        String password
) {
}
