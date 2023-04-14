package com.example.reactiveyamlgen.exception.handler;

import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class YamlExceptionHandler {


    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        CustomErrorResponse response = new CustomErrorResponse("Validation Failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResourceAccessException(ResponseStatusException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("Config Server is Down", errors);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("routeDtos cannot be null", errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIllegalIOException(IOException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("file write fail", errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
