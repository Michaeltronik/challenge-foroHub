package com.alura.foroHub.errores;

import com.alura.foroHub.domain.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("El JSON proporcionado no es válido.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Iterar sobre los errores de validación y construir el mapa de errores
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Obtener el mensaje de la excepción
        String errorMessage = ex.getCause().getMessage();

        // Utilizar expresión regular para extraer el nombre del campo (columna)
        String fieldName = extractFieldNameFromErrorMessage(errorMessage);

        // Si encontramos el campo que incumple, devolver un mensaje específico
        if (fieldName != null) {
            return "Error: El valor del campo '" + fieldName + "' ya existe en la base de datos. Por favor, ingrese un dato válido.";
        }

        return "Error: Se ha producido un error al procesar la solicitud.";
    }

    // Método para extraer el nombre del campo del mensaje de error
    private String extractFieldNameFromErrorMessage(String errorMessage) {
        // Expresión regular para capturar el nombre del campo
        String regex = "Duplicate entry '.*' for key '.*\\.(.*)'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(errorMessage);

        // Si encontramos el campo en el mensaje, lo extraemos
        if (matcher.find()) {
            return matcher.group(1); // Retorna el nombre del campo
        }
        return null;
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        // Verificamos si el mensaje contiene 'id'
        if (ex.getMessage().contains("id")) {
            // Retornamos el mensaje personalizado para el campo 'id'
            String message = "El valor ingresado para el campo 'id' no existe en la base de datos.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        // En caso de que no sea relacionado con 'id', retornamos un mensaje genérico
        String message = "El valor solicitado no existe en la base de datos.";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getReason());
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        // Devuelve solo el mensaje de la excepción
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}

