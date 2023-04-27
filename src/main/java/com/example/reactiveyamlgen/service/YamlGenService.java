package com.example.reactiveyamlgen.service;

import com.example.reactiveyamlgen.dto.ArgsDto;
import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.jpa.entity.Args;
import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.example.reactiveyamlgen.jpa.entity.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.List;

public interface YamlGenService {
    Flux<Void> saveYaml(List<RouteDto> dto);
    Flux<Tuple3<Route, FilterAndPredicate, Args>> readYaml();
    Mono<List<RouteDto>> getYaml();
    Mono<Void> writeYaml(List<RouteDto> routeDtos, List<FilterAndPredicateDto> filterAndPredicateDtos, List<ArgsDto> argsDtos) throws RouteNotFoundException, YamlFileIoException;
}
