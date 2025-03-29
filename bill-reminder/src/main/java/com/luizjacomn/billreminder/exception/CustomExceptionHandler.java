package com.luizjacomn.billreminder.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

    private static final String ERRORS = "errors";

    private static final String ERROR = "error";

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

        if (!fieldErrors.isEmpty()) {
            body.put(FIELDS, fieldErrors);
        }

        var globalErrors = ex.getBindingResult().getGlobalErrors();
        if (!globalErrors.isEmpty()) {
            var errors = globalErrors.stream().map(
                globalError ->
                    Map.of(
                        ERROR, globalError.getDefaultMessage()
                    )
            ).toList();

            body.put(ERRORS, errors);
        }

        return new ResponseEntity<>(body, headers, status);
    }

    private ResponseEntity<Object> handleKnownRuntime(RuntimeException ex, HttpStatus status) {
        Map<String, Object> body = Map.of(
            STATUS, status.value(),
            MESSAGE, ex.getMessage()
        );

        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

}
