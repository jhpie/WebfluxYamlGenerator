package com.example.reactiveyamlgen.controller;

import com.example.reactiveyamlgen.dto.ArgsDto;
import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.dto.ValidList;
import com.example.reactiveyamlgen.exception.exception.RouteNotFoundException;
import com.example.reactiveyamlgen.exception.exception.YamlFileIoException;
import com.example.reactiveyamlgen.service.YamlGenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(YamlGenController.class)
public class YamlGenControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private YamlGenService yamlGenService;



    ValidList<RouteDto> routeDtos;
    List<FilterAndPredicateDto> filterDtoList;
    List<FilterAndPredicateDto> predicateDtoList;
    List<ArgsDto> filterArgsList;
    List<ArgsDto> predicateArgsList;
    ArgsDto filterArgsDto;
    ArgsDto predicateArgsDto;
    FilterAndPredicateDto filterDto;
    FilterAndPredicateDto predicateDto;
    RouteDto routeDto;

    @BeforeEach
    public void init() {
        routeDtos = new ValidList<>();
        filterDtoList = new ArrayList<>();
        predicateDtoList = new ArrayList<>();
        filterArgsList = new ArrayList<>();
        predicateArgsList = new ArrayList<>();

        filterArgsDto = new ArgsDto();
        filterArgsDto.setHashKey("Path");
        filterArgsDto.setHashValue("/test");
        filterArgsDto.setParentName("testFilter");
        filterArgsDto.setRouteId("route1");
        filterArgsList.add(filterArgsDto);

        predicateArgsDto = new ArgsDto();
        predicateArgsDto.setHashKey("Path");
        predicateArgsDto.setHashValue("/test");
        predicateArgsDto.setParentName("testPredicate");
        predicateArgsDto.setRouteId("route1");
        predicateArgsList.add(predicateArgsDto);

        filterDto = new FilterAndPredicateDto();
        filterDto.setRouteId("1");
        filterDto.setIsFilter(true);
        filterDto.setIsName(true);
        filterDto.setName("filter");
        filterDto.setArgs(filterArgsList);
        filterDtoList.add(filterDto);

        predicateDto = new FilterAndPredicateDto();
        predicateDto.setRouteId("1");
        predicateDto.setIsFilter(false);
        predicateDto.setIsName(true);
        predicateDto.setName("predicate");
        predicateDto.setArgs(predicateArgsList);
        predicateDtoList.add(predicateDto);


        routeDto = new RouteDto();
        routeDto.setRouteId("route1");
        routeDto.setDomainId(1L);
        routeDto.setUri("/test");
        routeDto.setFilters(filterDtoList);
        routeDto.setPredicates(predicateDtoList);
        routeDto.setMetadata("for Test ROUTE");

        routeDtos.add(routeDto);
    }




    @Nested
    @DisplayName("/create")
    class Create {
        @Test
        @DisplayName("성공")
        void testCreateSuccess() {
            Mockito.when(yamlGenService.saveYaml(routeDtos)).thenReturn(Flux.empty());

            webTestClient.post().uri("/yaml/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(routeDtos))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Void.class);
        }

        @Test
        @DisplayName("실패")
        void testCreateFailure() {
            routeDtos.clear();

            webTestClient.post().uri("/yaml/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(routeDtos))
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("responseCode").isEqualTo("Validation Failed")
                    .jsonPath("errors").isEqualTo("list: RouteList가 Null이 되어선 안됩니다");
        }
    }
    @Nested
    @DisplayName("/write")
    class Write {
        @Test
        @DisplayName("성공")
        void testWriteSuccess() throws YamlFileIoException, RouteNotFoundException {
            // yamlGenService의 readYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.readYaml()).thenReturn(Flux.empty());

            // yamlGenService의 writeYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.writeYaml(Mockito.anyList(), Mockito.anyList(), Mockito.anyList())).thenReturn(Mono.empty());

            webTestClient.post().uri("/yaml/write")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Void.class);
        }

        @Test
        @DisplayName("실패")
        void testWriteFailure() throws YamlFileIoException, RouteNotFoundException {
            // yamlGenService의 readYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.readYaml()).thenReturn(Flux.empty());

            // yamlGenService의 writeYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.writeYaml(Mockito.anyList(), Mockito.anyList(), Mockito.anyList())).thenReturn(Mono.empty());

            webTestClient.post().uri("/yaml/write")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Void.class);
        }

        @Test
        @DisplayName("Route Not Found 예외 처리")
        void testWriteRouteNotFoundException() throws RouteNotFoundException, YamlFileIoException {
            Mockito.when(yamlGenService.readYaml()).thenReturn(Flux.empty());

            Mockito.when(yamlGenService.writeYaml(Mockito.anyList(), Mockito.anyList(), Mockito.anyList())).thenThrow(new RouteNotFoundException("Route not found"));

            webTestClient.post().uri("/yaml/write")
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectBody(String.class)
                    .value(responseBody -> assertTrue(responseBody.contains("Route not found")));
        }
    }


}

