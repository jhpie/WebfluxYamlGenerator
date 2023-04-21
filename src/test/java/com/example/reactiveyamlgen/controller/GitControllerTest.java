package com.example.reactiveyamlgen.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(GitController.class)
public class GitControllerTest {
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void testOnRefreshWhenYamlFileExists() throws Exception {
        // given
        Path resultFilePath = Paths.get("C:\\IdeaProjects\\reactive-yaml-gen\\src\\main\\resources\\result.yml");

        // 파일 존재여부
        if(!Files.exists(resultFilePath))
            Files.createFile(resultFilePath);
        // when
        webTestClient.post().uri("/git/refresh")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class).returnResult();

    }


    @Test
    void testOnRefreshWhenYamlFileDoesNotExist() throws Exception {
        // given
        Path tempDirectoryPath = Paths.get("src/test/resources/temp");
        Path resultFilePath = Paths.get("C:\\IdeaProjects\\reactive-yaml-gen\\src\\main\\resources\\result.yml");

        // 파일 삭제
        Files.deleteIfExists(Paths.get("C:\\IdeaProjects\\reactive-yaml-gen\\src\\main\\resources\\result.yml"));
        Files.deleteIfExists(Paths.get(tempDirectoryPath.toString(), "result_20220419120000.yml"));
        Files.deleteIfExists(tempDirectoryPath);

        // when
        webTestClient.post().uri("/git/refresh")
                .exchange()

                // then
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(responseBody -> assertTrue(responseBody.contains("result.yml file not found")));

    }
}


