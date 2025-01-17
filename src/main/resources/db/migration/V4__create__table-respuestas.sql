CREATE TABLE respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,  -- El mensaje de la respuesta
    fecha_de_creacion DATETIME NOT NULL,  -- Fecha de creación de la respuesta
    topico_id BIGINT NOT NULL,  -- Relación con la tabla topicos
    autor_id BIGINT NOT NULL,  -- Relación con la tabla usuarios
    CONSTRAINT FK_respuestas_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT FK_respuestas_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
