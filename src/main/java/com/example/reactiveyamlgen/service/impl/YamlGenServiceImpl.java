package com.example.reactiveyamlgen.service.impl;

import com.example.reactiveyamlgen.dto.ArgsDto;
import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.jpa.entity.Args;
import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.example.reactiveyamlgen.jpa.entity.Route;
import com.example.reactiveyamlgen.jpa.repository.ArgsRepository;
import com.example.reactiveyamlgen.jpa.repository.FilterAndPredicateRepository;
import com.example.reactiveyamlgen.jpa.repository.RouteRepository;
import com.example.reactiveyamlgen.service.YamlGenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class YamlGenServiceImpl implements YamlGenService {
    private final RouteRepository routeRepository;
    private final FilterAndPredicateRepository filterAndPredicateRepository;
    private final ArgsRepository argsRepository;
    private static final Logger logger = LogManager.getLogger(YamlGenServiceImpl.class);

    @Value("${target.directory}")
    private String TARGET_DIRECTORY_PATH;


    public Flux<Void> saveYaml(List<RouteDto> routeDtos) {
        Mono<Void> deleteAllMono = Mono.when(routeRepository.deleteAll(),
                filterAndPredicateRepository.deleteAll(),
                argsRepository.deleteAll());

        return deleteAllMono.thenMany(
                Flux.fromIterable(routeDtos)
                        .flatMap(dto -> routeRepository.existsByRouteId(dto.getRouteId())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new IllegalArgumentException("Duplicate routeId found: " + dto.getRouteId()));
                                    } else {
                                        Route route = new Route(dto);
                                        List<FilterAndPredicate> filterList = dto.getFilters().stream().map(FilterAndPredicate::new).collect(Collectors.toList());
                                        List<FilterAndPredicate> predicateList = dto.getPredicates().stream().map(FilterAndPredicate::new).collect(Collectors.toList());
                                        List<Args> filterArgsList = dto.getFilters().stream()
                                                .flatMap(filterDto -> filterDto.getArgs().stream().map(Args::new))
                                                .collect(Collectors.toList());
                                        List<Args> predicateArgsList = dto.getPredicates().stream()
                                                .flatMap(filterDto -> filterDto.getArgs().stream().map(Args::new))
                                                .collect(Collectors.toList());
                                        List<Args> argsList = Stream.concat(filterArgsList.stream(), predicateArgsList.stream())
                                                .collect(Collectors.toList());
                                        List<FilterAndPredicate> filterAndPredicateList = Stream.concat(filterList.stream(), predicateList.stream())
                                                .collect(Collectors.toList());

                                        Mono<Route> routeMono = routeRepository.save(route);
                                        Flux<FilterAndPredicate> filterAndPredicateListMono = filterAndPredicateRepository.saveAll(filterAndPredicateList);
                                        Flux<Args> argsListMono = argsRepository.saveAll(argsList);

                                        return Mono.when(routeMono, filterAndPredicateListMono, argsListMono);
                                    }
                                }))
                        .then()
        );
    }

    public Flux<Tuple3<Route, FilterAndPredicate, Args>> readYaml() {
        return routeRepository.findAll()
                .flatMap(route -> filterAndPredicateRepository.findAllByRouteId(route.getRouteId())
                        .map(filterAndPredicate -> Tuples.of(route, filterAndPredicate)))
                .flatMap(routeAndFilter -> argsRepository.findAllByParentName(routeAndFilter.getT2().getName())
                        .map(args -> Tuples.of(routeAndFilter.getT1(), routeAndFilter.getT2(), args)))
                .doOnNext(item -> logger.info("Item: " + item.toString()))
                .switchIfEmpty(Mono.error(new RouteNotFoundException("No routes found In DB")));
    }

    public Mono<List<RouteDto>> getYaml() {
        return readYaml()
                .collectList()
                .flatMap(tupleList -> {
                    List<ArgsDto> argsDtos = new ArrayList<>();
                    List<FilterAndPredicateDto> filterAndPredicateDtos = new ArrayList<>();
                    List<RouteDto> routeDtos = new ArrayList<>();

                    for (Tuple3<Route, FilterAndPredicate, Args> tuple : tupleList) {
                        RouteDto routeDto = new RouteDto(tuple.getT1());
                        if (!routeDtos.contains(routeDto)) {
                            routeDtos.add(routeDto);
                        }

                        FilterAndPredicateDto filterAndPredicateDto = new FilterAndPredicateDto(tuple.getT2());
                        filterAndPredicateDtos.add(filterAndPredicateDto);

                        ArgsDto argsDto = new ArgsDto(tuple.getT3());
                        argsDtos.add(argsDto);
                    }

                    Map<Boolean, List<FilterAndPredicateDto>> filterDtoMap = filterAndPredicateDtos.stream()
                            .collect(Collectors.partitioningBy(FilterAndPredicateDto::getIsFilter));

                    List<FilterAndPredicateDto> filterDtos = filterDtoMap.get(Boolean.TRUE);
                    List<FilterAndPredicateDto> predicateDtos = filterDtoMap.get(Boolean.FALSE);

                    for (FilterAndPredicateDto filterDto : filterDtos) {
                        List<ArgsDto> matchingArgs = argsDtos.stream()
                                .filter(argsDto -> argsDto.getParentName().equals(filterDto.getName()))
                                .collect(Collectors.toList());
                        filterDto.setArgs(matchingArgs);
                    }

                    for (FilterAndPredicateDto predicateDto : predicateDtos) {
                        List<ArgsDto> matchingArgs = argsDtos.stream()
                                .filter(argsDto -> argsDto.getParentName().equals(predicateDto.getName()))
                                .collect(Collectors.toList());
                        predicateDto.setArgs(matchingArgs);
                    }

                    routeDtos.forEach(routeDto -> {
                        String routeId = routeDto.getRouteId();
                        routeDto.setFilters(filterDtos.stream()
                                .filter(filterDto -> filterDto.getRouteId().equals(routeId))
                                .collect(Collectors.toList()));
                        routeDto.setPredicates(predicateDtos.stream()
                                .filter(predicateDto -> predicateDto.getRouteId().equals(routeId))
                                .collect(Collectors.toList()));
                    });

                    return Mono.just(routeDtos);
                })
                .switchIfEmpty(Mono.error(new RouteNotFoundException("No routes found in DB")));
    }

    @Override
    public Mono<Void> deleteYaml() {
        return Mono.when(routeRepository.deleteAll(),
                filterAndPredicateRepository.deleteAll(),
                argsRepository.deleteAll());
    }

    public Mono<Void> writeYaml(List<RouteDto> routeDtos, List<FilterAndPredicateDto> filterAndPredicateDtos, List<ArgsDto> argsDtos) throws RouteNotFoundException, YamlFileIoException {
        if (routeDtos.isEmpty()) {
            throw new RouteNotFoundException("No routes found In List<RouteDto>");
        }

        try {
            //파일객체 생성
            File file = new File(TARGET_DIRECTORY_PATH + "result.yml");
            FileWriter writer = new FileWriter(file, false);
            writer.write(
                    "spring:\n" +
                            "  cloud:\n" +
                            "    gateway:\n" +
                            "      routes:\n");
            for (RouteDto routeDto : routeDtos) {

                writer.write("      - id: " + routeDto.getRouteId() + "\n");
                writer.write("        uri: " + routeDto.getUri() + "\n");

                StringBuilder filterBuilder = new StringBuilder();
                StringBuilder predicateBuilder = new StringBuilder();
                filterAndPredicateDtos.stream()
                        .filter(filterAndPredicateDto -> filterAndPredicateDto.getRouteId().equals(routeDto.getRouteId()))
                        .forEach(filterAndPredicateDto -> {
                            if(filterAndPredicateDto.getIsFilter()==Boolean.TRUE) {
                                if(filterAndPredicateDto.getIsName()==Boolean.TRUE) {
                                    filterBuilder.append("        - name: ").append(filterAndPredicateDto.getName()).append("\n");
                                }else{
                                    filterBuilder.append("        - ").append(filterAndPredicateDto.getName()).append("\n");
                                }
                                filterBuilder.append("          args:\n");
                                for (ArgsDto argsDto : argsDtos) {
                                    if (argsDto.getParentName().equals(filterAndPredicateDto.getName()) && argsDto.getRouteId().equals(filterAndPredicateDto.getRouteId())) {
                                        filterBuilder.append("            ").append(argsDto.getHashKey()).append(": ").append(argsDto.getHashValue()).append("\n");
                                    }
                                }
                            }else{
                                if(filterAndPredicateDto.getIsName()==Boolean.TRUE) {
                                    predicateBuilder.append("        - name: ").append(filterAndPredicateDto.getName()).append("\n");
                                }else{
                                    predicateBuilder.append("        - ").append(filterAndPredicateDto.getName()).append("\n");
                                }
                                predicateBuilder.append("          args:\n");
                                for (ArgsDto argsDto : argsDtos) {
                                    if (argsDto.getParentName().equals(filterAndPredicateDto.getName()) && argsDto.getRouteId().equals(filterAndPredicateDto.getRouteId())) {
                                        // Write ArgsDto content
                                        predicateBuilder.append("            ").append(argsDto.getHashKey()).append(": ").append(argsDto.getHashValue()).append("\n");
                                    }
                                }
                            }
                        });

                if(filterBuilder.length()>0) {
                    filterBuilder.insert(0, "        filters:\n");
                }
                if(predicateBuilder.length()>0) {
                    predicateBuilder.insert(0, "        predicate:\n");
                }
                writer.write(String.valueOf(filterBuilder));
                writer.write(String.valueOf(predicateBuilder));
                filterBuilder.delete(0, filterBuilder.length());
                predicateBuilder.delete(0, filterBuilder.length());
            }
            writer.flush();
        } catch (IOException e) {
            throw new YamlFileIoException("Error occurred while writing YAML file", e);
        }
        return Mono.fromRunnable(() -> {

            logger.info("YamlGenServiceImpl-writeYaml()-DONE");
        });
    }
}
