package com.example.reactiveyamlgen.controller;

import com.example.reactiveyamlgen.config.Subscriber;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.jpa.entity.Args;
import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.example.reactiveyamlgen.jpa.entity.Route;
import com.example.reactiveyamlgen.service.YamlGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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

    @PostMapping("/create")
    private Flux<Void> create(@RequestBody List<RouteDto> routeDtos) {
        return yamlGenService.saveYaml(routeDtos);
    }

    @PostMapping("/write")
    public Mono<Void> write() {
        Flux<Tuple3<Route, FilterAndPredicate, Args>> routeFlux = yamlGenService.readYaml();
        Subscriber subscriber = new Subscriber();
        routeFlux.subscribe(subscriber);
        return routeFlux
                .then(yamlGenService.writeYaml(subscriber.getRouteDtos(), subscriber.getFilterAndPredicateDtos(), subscriber.getArgsDtos()))
                .then(Mono.fromRunnable(subscriber::clearDtos));
    }

    @PostMapping("/refresh")
    public void refresh() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

        String url = "http://localhost:8888/config/refresh";
        restTemplate.postForObject(url, requestEntity, Void.class);
    }
}