package com.example.reactiveyamlgen.exception.exception;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class YamlFileNotFoundException extends Exception{
    public YamlFileNotFoundException(String message) {
        super(message);
    }

    public YamlFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

