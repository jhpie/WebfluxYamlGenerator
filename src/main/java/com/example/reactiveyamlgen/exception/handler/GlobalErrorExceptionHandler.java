package com.example.reactiveyamlgen.exception.handler;

import com.example.reactiveyamlgen.exception.code.ErrorCode;
import com.example.reactiveyamlgen.exception.exception.CustomErrorResponseDto;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("errorWebExceptionHandler")
@Order(-2)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorExceptionHandler(GlobalErrorAttributes globalErrorAttributes,
                                       ApplicationContext applicationContext,
                                       ServerCodecConfigurer serverCodecConfigurer) {
        super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        if (error instanceof WebExchangeBindException) {
            Map<String, Object> errorProperties = getErrorAttributes(request, ErrorAttributeOptions.defaults());
            WebExchangeBindException bindException = (WebExchangeBindException) error;
            BindingResult bindingResult = bindException.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            List<String> errorMessages = new ArrayList<>();
            StringBuilder result = new StringBuilder();
            for (ObjectError objectError : allErrors) {
                errorMessages.add(objectError.getDefaultMessage());
                result.append(objectError.getDefaultMessage()+"\n");
            }
            System.out.println(result);
            CustomErrorResponseDto response = new CustomErrorResponseDto();
            response.setPath(request.path());
            response.setError(String.valueOf(result));
            response.setException(error.getClass().getSimpleName());
            response.setMessage(ErrorCode.VALIDATION_ERROR.getMessage());
            response.setErrorCode(ErrorCode.VALIDATION_ERROR.getCode());

            return ServerResponse.status(((WebExchangeBindException) error).getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(response));
        } else {
            Map<String, Object> errorProperties = getErrorAttributes(request, ErrorAttributeOptions.defaults());
            CustomErrorResponseDto response = new CustomErrorResponseDto();
            response.setPath((String) errorProperties.get("path"));
            response.setError((String) errorProperties.get("error"));
            response.setException(error.getClass().getSimpleName());
            response.setMessage((String) errorProperties.get("message"));
            response.setErrorCode((String) errorProperties.get("errorCode"));
            return ServerResponse.status(Integer.parseInt(errorProperties.get("status").toString()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(response));
        }
    }


}