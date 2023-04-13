package com.example.reactiveyamlgen.exception.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomErrorResponse {
    private String responseCode;
    private List<String> errors;

    public CustomErrorResponse(String responseCode, List<String> errors) {
        this.responseCode = responseCode;
        this.errors = errors;
    }
}
