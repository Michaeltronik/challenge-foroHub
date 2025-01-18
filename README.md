# Foro Hub

Foro Hub es una aplicación básica de foro desarrollada con **Spring Boot** y **Maven**. El objetivo principal de esta aplicación es proporcionar una plataforma para gestionar usuarios y tópicos a través de operaciones HTTP. Además, se implementó autenticación utilizando JWT para garantizar la seguridad.

---

## Características Principales

1. **Gestión de Usuarios**:
   - Registrar un nuevo usuario.
   - Actualizar información de un usuario existente.
   - Listar todos los usuarios registrados.
   - Buscar un usuario por su ID.
   - Eliminar usuarios (eliminación lógica, no se borra de la base de datos).

2. **Gestión de Tópicos**:
   - Registrar un nuevo tópico.
   - Actualizar información de un tópico existente.
   - Listar todos los tópicos registrados.
   - Buscar un tópico por su ID.
   - Eliminar tópicos (eliminacion completa de la base de datos).
   - Desactivar tópicos(eliminación lógica).

3. **Autenticación y Seguridad**:
   - Implementación de autenticación mediante **JSON Web Tokens (JWT)**.
   - Uso de **Spring Security** para garantizar acceso seguro a los recursos.

---

## Tecnologías y Herramientas Utilizadas

- **Java**: Versión 17 (JDK 17).
- **Spring Boot**: Framework principal para la construcción de la aplicación.
- **Maven**: Herramienta de gestión de dependencias y construcción del proyecto.

### Dependencias Principales

1. **Spring Boot Starter Web**: Para manejar solicitudes HTTP y crear endpoints RESTful.
2. **Spring Security**: Implementación de seguridad para la autenticación y autorización.
3. **Auth0 JWT**: Generación y validación de tokens JWT.
4. **Spring Boot Starter Validation**: Validación de datos en las solicitudes.
5. **MySQL Driver**: Conexión con la base de datos MySQL.
6. **Lombok**: Simplificación del código eliminando la necesidad de escribir getters, setters y constructores.
7. **Spring Boot DevTools**: Herramientas para facilitar el desarrollo, como el reinicio automático.
8. **Flyway**: Gestión de migraciones de base de datos.
9. **Spring Data JPA**: Acceso y manipulación de datos en la base de datos.

---

## Instalación y Configuración

### Requisitos Previos

1. **Java Development Kit (JDK)** versión 17 o superior.
2. **Maven** .
3. **MySQL** configurado y en ejecución.
4. Un entorno de desarrollo como **IntelliJ IDEA**.

### Configuración del Proyecto

1. Clona este repositorio:

   ```bash
   git clone <URL-del-repositorio>
   ```

2. Ingresa al directorio del proyecto:

   ```bash
   cd foro-hub
   ```

3. Configura las credenciales de la base de datos en el archivo `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/foro_hub
   spring.datasource.username=<usuario>
   spring.datasource.password=<contraseña>
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.flyway.enabled=true
   api.security.secret=${JWT_SECRET}
   ```


## Endpoints Principales

### Usuarios

- **POST** `/usuarios`: Registrar un nuevo usuario.
- **GET** `/usuarios`: Listar todos los usuarios.
- **GET** `/usuarios/{id}`: Buscar usuario por ID.
- **PUT** `/usuarios/{id}`: Actualizar usuario.
- **DELETE** `/usuarios/{id}`: Eliminar usuario (lógica).

### Tópicos

- **POST** `/topicos`: Registrar un nuevo tópico.
- **GET** `/topicos`: Listar todos los tópicos.
- **GET** `/topicos/{id}`: Buscar tópico por ID.
- **PUT** `/topicos/{id}`: Actualizar tópico.
- **DELETE** `/topicos/{id}/eliminar`: Eliminar tópico (lógica).
- **DELETE** `/topicos/{id}/desactivar`: Desactivar tópico.

---

## Mejoras Pendientes

1. **Gestión de Excepciones**:
   - Mejorar las respuestas proporcionadas al manejar excepciones para ofrecer información más clara y útil.

2. **Internacionalización**:
   - Adaptar el idioma de la aplicación a un estándar como inglés para mayor alcance.

3. **Extensión de Endpoints**:
   - Implementar endpoints adicionales para gestionar respuestas y cursos.

4. **Documentación con Swagger**:
   - Integrar Swagger para documentar de manera más clara y accesible los endpoints y sus funcionalidades.

---

