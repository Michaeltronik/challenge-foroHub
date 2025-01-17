package com.alura.foroHub.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopicos(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaDeCreacion,
        Boolean estado,
        String autor,
        String curso
) {
    public DatosListadoTopicos (Topico topico){
        this(topico.getId(), topico.getTitulo(),topico.getMensaje(),topico.getFechaDeCreacion(),topico.isEstado(), topico.getAutor(), topico.getCurso());
    }
}
