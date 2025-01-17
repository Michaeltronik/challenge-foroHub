CREATE TABLE topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensaje VARCHAR(300) NOT NULL,
    fecha_de_creacion DATETIME NOT NULL,
    estado BOOLEAN NOT NULL,
    autor VARCHAR(255) NOT NULL,
    curso VARCHAR(255) NOT NULL,
    respuestas_count INT DEFAULT 0,

    CONSTRAINT UC_topicos_titulo UNIQUE (titulo),
    CONSTRAINT UC_topicos_mensaje UNIQUE (mensaje)


);

