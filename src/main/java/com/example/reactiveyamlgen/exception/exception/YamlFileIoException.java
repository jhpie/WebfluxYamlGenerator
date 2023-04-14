package com.example.reactiveyamlgen.exception.exception;

public class YamlFileIoException extends Exception {
    public YamlFileIoException(String message) {
        super(message);
    }

    public YamlFileIoException(String message, Throwable cause) {
        super(message, cause);
    }
}
