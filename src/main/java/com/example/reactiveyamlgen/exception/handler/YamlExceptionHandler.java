//package com.example.reactiveyamlgen.exception.handler;
//
//import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
//import com.example.reactiveyamlgen.exception.exception.SubscriberException;
//import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
//import com.example.reactiveyamlgen.exception.exception.YamlFileNotFoundException;
//import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.bind.support.WebExchangeBindException;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
////WebFlux의 관점에서 WebFlux 애플리케이션에서 @ExceptionHandler와 함께 @ControllerAdvice를 사용하면 이점이 있습니다.
////WebFlux는 반응형 프로그래밍 모델을 기반으로 하므로 비차단 방식을 사용하여 요청 및 응답을 처리합니다. 요청 처리 중에 예외가 발생하면 기존의 서블릿 기반 예외 처리 접근 방식은 예외가 처리될 때까지 요청 스레드를 차단하므로 성능 문제가 발생하고 전체 애플리케이션 속도가 느려질 수 있습니다.
////그러나 WebFlux의 반응형 모델을 사용하면 예외가 처리되는 동안 애플리케이션이 요청을 계속 처리할 수 있으므로 리소스 활용도와 애플리케이션 성능이 향상됩니다.
////WebFlux 애플리케이션에서 @ExceptionHandler와 함께 @ControllerAdvice를 사용하면 비차단 방식으로 예외를 처리하고 Mono 또는 Flux와 같은 반응형 응답을 클라이언트에 반환할 수 있습니다. 이를 통해 리소스를 보다 효율적으로 사용하고 높은 동시성 시나리오를 보다 잘 처리할 수 있습니다.
//
//
//@RestControllerAdvice
//@Slf4j
//public class YamlExceptionHandler {
//
//
//    @ExceptionHandler(WebExchangeBindException.class)
//    public ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
//        List<String> errors = ex.getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .toList();
//
//        CustomErrorResponse response = new CustomErrorResponse("Validation Failed", errors);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Object> handleResourceAccessException(ResponseStatusException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("Config Server is Down", errors);
//        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("routeDtos cannot be null", errors);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<Object> handleIllegalIOException(IOException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("file write fail", errors);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(SubscriberException.class)
//    public ResponseEntity<Object> handleSubscriberException(SubscriberException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("Subscriber error", errors);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(RouteNotFoundException.class)
//    public ResponseEntity<Object> handleRouteNotFoundException(RouteNotFoundException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("Null in DB", errors);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(YamlFileIoException.class)
//    public ResponseEntity<Object> handleYamlFileIoException(YamlFileIoException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("file write fail", errors);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//    @ExceptionHandler(YamlFileNotFoundException.class)
//    public ResponseEntity<Object> handleFileNotFoundException(YamlFileNotFoundException ex) {
//        String errorsMessage = ex.getMessage();
//        List<String> errors = new ArrayList<>();
//        errors.add(errorsMessage);
//        CustomErrorResponse response = new CustomErrorResponse("yaml file not found", errors);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//
//}
