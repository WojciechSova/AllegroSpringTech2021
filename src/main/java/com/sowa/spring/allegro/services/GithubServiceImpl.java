package com.sowa.spring.allegro.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowa.spring.allegro.domain.model.GithubRepository;
import com.sowa.spring.allegro.services.interfaces.GithubService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GithubServiceImpl implements GithubService {

    @Value("${url.toGetAllRepositories.beforeUsername}")
    private String URL_BEFORE_USERNAME_TO_GET_ALL_REPOSITORIES;

    @Value("${url.toGetAllRepositories.afterUsername}")
    private String URL_AFTER_USERNAME_TO_GET_ALL_REPOSITORIES;

    private final Logger logger = LoggerFactory.getLogger(GithubServiceImpl.class);

    @Override
    public List<GithubRepository> getRepositoriesFromGithubByUsername(String githubUsername) {
        return mapJsonToGithubRepositoryList(sendRequestAndGetResponseBody(URL_BEFORE_USERNAME_TO_GET_ALL_REPOSITORIES
                + githubUsername + URL_AFTER_USERNAME_TO_GET_ALL_REPOSITORIES));
    }

    @Override
    public Integer getSumOfAllStarsInRepositories(String githubUsername) {
        List<GithubRepository> githubRepositories = getRepositoriesFromGithubByUsername(githubUsername);
        return githubRepositories.stream().mapToInt(GithubRepository::getStars).sum();
    }

    private List<GithubRepository> mapJsonToGithubRepositoryList(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<GithubRepository> githubRepositories;
        try {
            githubRepositories = objectMapper.readValue(responseBody, new TypeReference<List<GithubRepository>>(){});
        } catch (JsonProcessingException e) {
            logger.error("Error while mapping json to GithubRepository list: " +  e);
            throw new RuntimeException(e.getMessage());
        }
        return githubRepositories;
    }

    private String sendRequestAndGetResponseBody(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).string();
            }
            logger.error("Response was not successful: " +  response);
            throw new RuntimeException(response.message());
        } catch (IOException e) {
            logger.error("Error while executing request: " +  e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
