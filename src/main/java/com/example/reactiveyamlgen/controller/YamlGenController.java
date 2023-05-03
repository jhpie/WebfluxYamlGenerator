package com.example.reactiveyamlgen.controller;

import com.example.reactiveyamlgen.config.Subscriber;
import com.example.reactiveyamlgen.dto.CustomResponseDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.dto.RouteIdDto;
import com.example.reactiveyamlgen.dto.ValidList;
import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.jpa.entity.Args;
import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.example.reactiveyamlgen.jpa.entity.Route;
import com.example.reactiveyamlgen.service.YamlGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.List;

@RestController
@RequestMapping(value = "/yaml")
public class YamlGenController {
    private final YamlGenService yamlGenService;
    @Autowired
    public YamlGenController(YamlGenService yamlGenService) {
        this.yamlGenService = yamlGenService;
    }

    @PostMapping(value = "/create")
    public Mono<CustomResponseDto> create(@Validated @RequestBody ValidList<RouteDto> routeDtos) {
        return yamlGenService.saveYaml(routeDtos)
                .then(Mono.just(CustomResponseDto.builder().build()))
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

    @PostMapping(value = "/write")
    public Mono<CustomResponseDto> write() throws YamlFileIoException, RouteNotFoundException {
        Flux<Tuple3<Route, FilterAndPredicate, Args>> routeFlux = yamlGenService.readYaml();
        Subscriber subscriber = new Subscriber();
        routeFlux.subscribe(subscriber);
        return routeFlux
                .then(yamlGenService.writeYaml(subscriber.getRouteDtos(), subscriber.getFilterAndPredicateDtos(), subscriber.getArgsDtos()))
                .then(Mono.fromRunnable(subscriber::clearDtos))
                .then(Mono.just(CustomResponseDto.builder().build()))
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

    @PostMapping(value = "/read")
    public Mono<CustomResponseDto> read() {
        return yamlGenService.getYaml()
                .map(routes -> CustomResponseDto.builder().responseMessage(String.valueOf(routes)).build())
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

    @PostMapping(value = "/update")
    public Mono<CustomResponseDto> update(@Validated @RequestBody ValidList<RouteDto> routeDtos) {
        return yamlGenService.updateYaml(routeDtos)
                .then(Mono.just(CustomResponseDto.builder().build()))
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

    @PostMapping(value = "/deleteAll")
    public Mono<CustomResponseDto> delete() {
        return yamlGenService.deleteYamlAll()
                .then(Mono.just(CustomResponseDto.builder().build()))
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

    @PostMapping(value = "/delete")
    public Mono<CustomResponseDto> deleteById(@RequestBody List<RouteIdDto> routeIdDtos) {
        return yamlGenService.deleteYamlById(routeIdDtos)
                .then(Mono.just(CustomResponseDto.builder().build()))
                .onErrorResume(ex -> Mono.just(CustomResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .responseMessage("Error message")
                        .build()));
    }

}