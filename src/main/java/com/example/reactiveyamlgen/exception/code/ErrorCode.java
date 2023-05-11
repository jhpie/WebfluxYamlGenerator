package com.example.reactiveyamlgen.exception.code;

import com.example.reactiveyamlgen.dto.RouteDto;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    VALIDATION_ERROR(400, "ER001", "Validation Failed"),
    CONFIG_SERVER_IS_DOWN(500, "ER002", "Config Server is Down"),
    ROUTE_DTOS_NULL_ERROR(400, "ER003", "RouteDtos cannot be null"),
    ROUTE_LIST_DTOS_NULL_ERROR(400, "ER004", "List< RouteDto > cannot be null"),
    INTERNAL_SERVER_ERROR(500, "ER005", "INTERNAL SERVER ERROR"),
    FILE_WRITE_FAIL_ERROR(503, "ER006", "Yaml File write fail"),
    SUBSCRIBER_ERROR(503, "ER007", "Subscriber error"),
    NULL_IN_DB(503, "ER008", "Null in DB"),
    NOT_FOUND_IN_DB(503, "ER009", "Not Found in DB"),
    FILE_NOT_FOUND_ERROR(503, "ER010", "Yaml File not Found")
    ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
