package com.example.reactiveyamlgen.exception.exception;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponseDto {
    private String path;
    private String error;
    private String exception;
    private String message;
    private String errorCode;

}