package com.example.reactiveyamlgen.jpa.repository;

import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FilterAndPredicateRepository extends ReactiveCrudRepository<FilterAndPredicate,Long> {
    Flux<FilterAndPredicate> findAllByRouteId(String routeId);

    Mono<Void> deleteByRouteId(String routeId);
}
