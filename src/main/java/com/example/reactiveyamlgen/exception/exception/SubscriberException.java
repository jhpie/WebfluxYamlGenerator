package com.example.reactiveyamlgen.exception.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriberException extends Exception {
    public SubscriberException(String message) {
        super(message);
    }

    public SubscriberException(String message, Throwable cause) {
        super(message, cause);
    }
}
