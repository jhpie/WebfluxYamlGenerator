package com.example.reactiveyamlgen.exception.handler;

import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class YamlExceptionHandler {

    private static final String CLOSE = "close";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        CustomErrorResponse response = new CustomErrorResponse("Validation Failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
