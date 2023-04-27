package com.example.reactiveyamlgen.controller;


import com.example.reactiveyamlgen.dto.MessageDto;
import com.example.reactiveyamlgen.exception.exception.YamlFileNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/git")
public class GitController {
    @Value("${spring.cloud.config.server.git.uri}")
    private String gitUri;

    @Value("${spring.cloud.config.server.git.search-paths}")
    private String sourceDirectory;

    @Value("${temp-path}")
    private String tempDirectory;

    private Git git;

    private static final Logger logger = LogManager.getLogger(GitController.class);


    @PostConstruct
    public void init() throws IOException, GitAPIException {
        Path destinationPath = Paths.get(tempDirectory); // 기존 경로
        File destinationFile = destinationPath.toFile();
        if (!destinationFile.exists()) {
            Git.cloneRepository()
                    .setURI(gitUri)
                    .setDirectory(destinationFile)
                    .setCloneAllBranches(false)
                    .call();
        }
        git = Git.open(destinationFile);
    }



    @PostMapping("/push")
    public Mono<String> pushToGitRepository(@RequestBody MessageDto messageDto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        Path tempFilePath = Paths.get(tempDirectory, "result_" + timestamp + ".yml");

        Path resultFilePath = Paths.get(sourceDirectory, "result.yml");
        if (Files.exists(resultFilePath)) {
            try {
                Files.copy(resultFilePath, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                git.add().addFilepattern(".").call();
                git.commit().setMessage(messageDto.getMessage()).call();
                git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", "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")).call();
                return Mono.just("Pushed to git repository successfully!");
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
                return Mono.error(e);
            }
        } else {
            logger.info("result.yml file not found");
            return Mono.error(new YamlFileNotFoundException("result.yml file not found"));
        }
    }

    @PostMapping(value = "/refresh")
    public Mono<String> refresh() {
        WebClient client = WebClient.create("http://127.0.0.1:8888");
        return client.post()
                .uri("/actuator/refresh")
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("Success")
                .onErrorResume(error -> Mono.just("Gateway Server is Down"));
    }

}
