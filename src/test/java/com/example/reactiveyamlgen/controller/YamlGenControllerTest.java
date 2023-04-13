package com.example.reactiveyamlgen.controller;

import com.example.reactiveyamlgen.dto.ArgsDto;
import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import com.example.reactiveyamlgen.dto.RouteDto;
import com.example.reactiveyamlgen.dto.ValidList;
import com.example.reactiveyamlgen.exception.response.CustomErrorResponse;
import com.example.reactiveyamlgen.service.YamlGenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(YamlGenController.class)
public class YamlGenControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private YamlGenService yamlGenService;

    @MockBean
    private RestTemplate restTemplate;
    //
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
        public void testCreateFailure() {
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
        public void testWriteSuccess() {
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
        public void testWriteFailure() {
            // yamlGenService의 readYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.readYaml()).thenReturn(Flux.empty());

            // yamlGenService의 writeYaml() 메소드 결과에 대한 Mock 데이터 설정
            Mockito.when(yamlGenService.writeYaml(Mockito.anyList(), Mockito.anyList(), Mockito.anyList())).thenReturn(Mono.empty());

            webTestClient.post().uri("/yaml/write")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Void.class);
        }
    }

    @Nested
    @DisplayName("/refresh")
    class Refresh {
        @Test
        @DisplayName("성공")
        public void testRefresh() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            String url = "http://localhost:8888/config/refresh";
            Mockito.when(restTemplate.postForObject(url, requestEntity, Void.class)).thenReturn(null);
        }
        @Test
        @DisplayName("실패")
        public void testRefreshFailure() {
            String url = "http://localhost:8888/config/refresh";
            Mockito.when(restTemplate.postForObject(any(), any(), any()))
                    .thenThrow(new ResourceAccessException("I/O error on POST request for \"http://localhost:8888/config/refresh\": Connection refused: connect"));



            // Send the request and check the response
            webTestClient.post().uri("/yaml/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is5xxServerError()
                    .expectBody()
                    .jsonPath("responseCode").isEqualTo("Config Server is Down")
                    .jsonPath("errors").isEqualTo("I/O error on POST request for \"http://localhost:8888/config/refresh\": Connection refused: connect");
        }


    }

}
