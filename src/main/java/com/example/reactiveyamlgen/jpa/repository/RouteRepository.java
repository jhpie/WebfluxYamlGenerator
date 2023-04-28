package com.example.reactiveyamlgen.jpa.repository;

import com.example.reactiveyamlgen.jpa.entity.Route;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RouteRepository extends ReactiveCrudRepository<Route,Long> {
    Mono<Boolean> existsByRouteId(String routeId);
    Mono<Route> findByRouteId(String routeId);
    Mono<Void> deleteByRouteId(String routeId);
}
