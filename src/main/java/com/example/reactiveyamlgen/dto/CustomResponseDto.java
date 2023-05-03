package com.example.reactiveyamlgen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomResponseDto{

    private String responseCode;
    private String responseMessage;
    private String refreshResponseCode;
    private String refreshResponseMessage;


    @Builder
    public CustomResponseDto(String responseCode,
                             String responseMessage,
                             String refreshResponseCode,
                             String refreshResponseMessage
    ) {
        this.responseCode = responseCode == null ? "200" : responseCode;
        this.responseMessage = responseMessage == null ? "요청이 성공적으로 되었습니다." : responseMessage;
        this.refreshResponseCode = refreshResponseCode;
        this.refreshResponseMessage = refreshResponseMessage;
    }
}