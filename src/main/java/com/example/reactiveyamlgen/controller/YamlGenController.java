package com.example.reactiveyamlgen.controller;

import com.example.reactiveyamlgen.config.Subscriber;
import com.example.reactiveyamlgen.dto.RouteDto;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;


@RestController
@RequestMapping(value = "/yaml")
public class YamlGenController {
    private final YamlGenService yamlGenService;
    @Autowired
    public YamlGenController(YamlGenService yamlGenService) {
        this.yamlGenService = yamlGenService;
    }

    @PostMapping(value = "/create")
    public Flux<Void> create(@Validated @RequestBody ValidList<RouteDto> routeDtos) {
        return yamlGenService.saveYaml(routeDtos);
    }

    @PostMapping(value = "/write")
    public Mono<Void> write() throws YamlFileIoException, RouteNotFoundException {
        Flux<Tuple3<Route, FilterAndPredicate, Args>> routeFlux = yamlGenService.readYaml();
        Subscriber subscriber = new Subscriber();
        routeFlux.subscribe(subscriber);
        return routeFlux
                .then(yamlGenService.writeYaml(subscriber.getRouteDtos(), subscriber.getFilterAndPredicateDtos(), subscriber.getArgsDtos()))
                .then(Mono.fromRunnable(subscriber::clearDtos));
    }

    @PostMapping(value = "/refresh")
    public Mono<Void> refresh() {
        WebClient client = WebClient.create("http://127.0.0.1:8888");
        return client.post()
                .uri("/yaml/refresh")
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Config Server is Down", error));
    }
}