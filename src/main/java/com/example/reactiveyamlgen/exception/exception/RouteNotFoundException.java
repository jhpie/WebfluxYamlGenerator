package com.example.reactiveyamlgen.exception.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteNotFoundException extends Exception {
    public RouteNotFoundException(String message) {
        super(message);
    }

    public RouteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}