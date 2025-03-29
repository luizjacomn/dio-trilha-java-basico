package com.luizjacomn.designpatterns.controller;

import com.luizjacomn.designpatterns.exception.AddressNotFoundException;
import com.luizjacomn.designpatterns.exception.ClientNotFoundException;
import com.luizjacomn.designpatterns.exception.InvalidLevelTransitionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String STATUS = "status";

    private static final String MESSAGE = "message";

    private static final String FIELDS = "fields";

    private static final String FIELD = "field";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(MESSAGE, "Erros de validações encontrados!");

        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError ->
                Map.of(
                    FIELD, fieldError.getField(),
                    MESSAGE, fieldError.getDefaultMessage()
                )
            ).toList();

        body.put(FIELDS, fieldErrors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ AddressNotFoundException.class, ClientNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return this.handleKnownRuntime(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ InvalidLevelTransitionException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return this.handleKnownRuntime(ex, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> handleKnownRuntime(RuntimeException ex, HttpStatus status) {
        Map<String, Object> body = Map.of(
            STATUS, status.value(),
            MESSAGE, ex.getMessage()
        );

        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

}
