package com.example.reactiveyamlgen.jpa.repository;

import com.example.reactiveyamlgen.jpa.entity.Args;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ArgsRepository extends ReactiveCrudRepository<Args,Long> {
    Flux<Args> findAllByParentName(String parentName);
}
