package com.example.reactiveyamlgen.config;

import com.example.reactiveyamlgen.dto.ArgsDto;
import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.jpa.entity.Args;
import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.example.reactiveyamlgen.jpa.entity.Route;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.util.function.Tuple3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Subscriber extends BaseSubscriber<Tuple3<Route, FilterAndPredicate, Args>> {

    private final List<RouteDto> routeDtos = new ArrayList<>();
    private final List<FilterAndPredicateDto> filterAndPredicateDtos = new ArrayList<>();
    private final List<ArgsDto> argsDtos = new ArrayList<>();
    private final AtomicInteger remaining = new AtomicInteger(0);

    public List<RouteDto> getRouteDtos() {
        return routeDtos.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<FilterAndPredicateDto> getFilterAndPredicateDtos() {
        return filterAndPredicateDtos.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<ArgsDto> getArgsDtos() {
        return argsDtos.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        remaining.incrementAndGet();
        request(Long.MAX_VALUE);
    }

    @Override
    protected void hookOnNext(Tuple3<Route, FilterAndPredicate, Args> tuple) {
        System.out.println("hookOnNext!!!!!!!!");
        Route route = tuple.getT1();
        FilterAndPredicate filterAndPredicate = tuple.getT2();
        Args args = tuple.getT3();

        routeDtos.add(route.toDto());
        filterAndPredicateDtos.add(filterAndPredicate.toDto());
        argsDtos.add(args.toDto());
        if (remaining.decrementAndGet() == 0) {
            request(0);
            hookOnComplete();
        } else {
            request(1);
        }
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        System.out.println("Error occurred: " + throwable.getMessage());
    }

    @Override
    protected void hookOnComplete() {

    }

    public Publisher<Void> clearDtos() {
        routeDtos.clear();
        filterAndPredicateDtos.clear();
        argsDtos.clear();
        return null;
    }


}
