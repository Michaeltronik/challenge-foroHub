package com.alura.foroHub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosRegistroTopico(
        @NotBlank(message = "titulo es un campo obligatorio")
        String titulo,
        @NotBlank(message = "mensaje es un campo obligatorio")
        String mensaje,
        @NotNull (message = "fechaDeCreacion es un campo obligatorio")
        LocalDateTime fechaDeCreacion,
        @NotNull (message = "autorId es un campo obligatorio")
        Long autorId,
        @NotNull (message = "cursoId es un campo obligatorio")
        Long cursoId
) {
}
