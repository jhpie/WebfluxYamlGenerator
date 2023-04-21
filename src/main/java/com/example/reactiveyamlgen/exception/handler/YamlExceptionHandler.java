package com.example.reactiveyamlgen.exception.handler;

import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.SubscriberException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.exception.exception.YamlFileNotFoundException;
import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class YamlExceptionHandler {


    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

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
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIllegalIOException(IOException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("file write fail", errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SubscriberException.class)
    public ResponseEntity<Object> handleSubscriberException(SubscriberException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("Subscriber error", errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<Object> handleRouteNotFoundException(RouteNotFoundException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("Null in DB", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(YamlFileIoException.class)
    public ResponseEntity<Object> handleYamlFileIoException(YamlFileIoException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("file write fail", errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(YamlFileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(YamlFileNotFoundException ex) {
        String errorsMessage = ex.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(errorsMessage);
        CustomErrorResponse response = new CustomErrorResponse("yaml file not found", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
