package com.example.reactiveyamlgen.jpa.repository;

import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FilterAndPredicateRepository extends ReactiveCrudRepository<FilterAndPredicate,Long> {
    Flux<FilterAndPredicate> findAllByRouteId(String routeId);
}
