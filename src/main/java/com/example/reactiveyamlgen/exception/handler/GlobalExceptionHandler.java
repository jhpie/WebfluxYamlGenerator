package com.example.reactiveyamlgen.exception.handler;

import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.SubscriberException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.exception.exception.YamlFileNotFoundException;
import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
//이 예제에서는 오류 응답을 포함하는 데이터 버퍼의 반응형 스트림을 생성하기 위해 Flux.just() 메서드를 사용하고 있습니다. 반응형 스트림을 사용하면 Webflux의 비차단 I/O 모델을 활용하여 스레드를 차단하지 않고 여러 요청을 동시에 처리할 수 있습니다.
//기존 접근 방식에서는 서버가 전체 응답을 생성한 다음 출력 스트림에 씁니다. 반응적 접근 방식에서 서버는 응답을 하나씩 생성하고 사용 가능해지면 각 데이터 조각을 출력 스트림에 기록합니다.
//
//
//'DataBuffer' API를 사용하여 서버는 전체 응답이 생성될 때까지 기다리지 않고 데이터가 사용 가능해지면 점차적으로 출력 스트림에 대한 응답을 작성할 수 있습니다. 이는 출력 스트림에 쓰기 전에 전체 응답이 생성될 때까지 기다리면 성능 문제가 발생할 수 있는 대규모 페이로드에 특히 중요할 수 있습니다.
@Component
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        String message;

        if (ex instanceof WebExchangeBindException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Validation Failed";
        } else if (ex instanceof ResponseStatusException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Config Server is Down";
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            message = "routeDtos cannot be null";
        } else if (ex instanceof IOException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "file write fail";
        } else if (ex instanceof SubscriberException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Subscriber error";
        } else if (ex instanceof RouteNotFoundException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Null in DB";
        } else if (ex instanceof YamlFileIoException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "file write fail";
        } else if (ex instanceof YamlFileNotFoundException) {
            status = HttpStatus.BAD_REQUEST;
            message = "yaml file not found";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal Server Error";
            logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(message, Collections.singletonList(ex.getMessage()));
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(new Gson().toJson(errorResponse).getBytes());
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}