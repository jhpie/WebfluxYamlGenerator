package com.example.reactiveyamlgen.exception.code;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    VALIDATION_ERROR(400, "VE001", "Validation Failed"),
    ROUTE_DTOS_NULL_ERROR(400, "VE002", "RouteDtos cannot be null"),
    ROUTE_LIST_DTOS_NULL_ERROR(400, "VE003", "List< RouteDto > cannot be null"),
    CONFIG_SERVER_IS_DOWN(503, "SE001", "Config Server is Down"),
    INTERNAL_SERVER_ERROR(500, "SE002", "Internal Server Error"),
    FILE_WRITE_FAIL_ERROR(503, "SE003", "YAML File Write Failed"),
    SUBSCRIBER_ERROR(503, "SE004", "Subscriber Error"),
    NULL_IN_DB(500, "SE005", "Null Value in Database"),
    NOT_FOUND_IN_DB(404, "NE001", "Item Not Found in Database"),
    FILE_NOT_FOUND_ERROR(404, "NE002", "YAML File Not Found")
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
