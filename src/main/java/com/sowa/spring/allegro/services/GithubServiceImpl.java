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

    @Override
    public Map<String, Integer> getLanguagesUsedByUser(String githubUsername) {
        Map<String, Integer> languages;
        List<GithubRepository> githubRepositories = getRepositoriesFromGithubByUsername(githubUsername);
        List<String> languageJsons = new ArrayList<>();

        githubRepositories.forEach(githubRepository ->
                languageJsons.add(sendRequestAndGetResponseBody(githubRepository.getLanguagesUrl()))
        );

        if (languageJsons.isEmpty()) {
            return null;
        }

        languages = mergeLanguagesJsonsToMap(languageJsons);
        return sortMapByValuesDesc(languages);
    }

    private Map<String, Integer> mergeLanguagesJsonsToMap(List<String> languageJsons) {
        Map<String, Integer> languages = new HashMap<>();
        languageJsons.forEach(language -> {
            Map<String, Integer> temp;
            try {
                temp = new ObjectMapper().readValue(language, Map.class);
            } catch (JsonProcessingException e) {
                logger.error("Error while mapping json to map " + e);
                throw new RuntimeException(e.getMessage());
            }
            temp.forEach((key, value) -> languages.merge(key, value, Integer::sum));
        });
        return languages;
    }


    public static <K, V extends Comparable<V> > Map<K, V> sortMapByValuesDesc(final Map<K, V> map)
    {
        Comparator<K> valueComparator = (v1, v2) -> {
            int comp = map.get(v2).compareTo(map.get(v1));
            if (comp == 0)
                return 1;
            else
                return comp;
        };
        Map<K, V> sorted = new TreeMap<>(valueComparator);
        sorted.putAll(map);
        return sorted;
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
            logger.error("Response was not successful: " +  Objects.requireNonNull(response.body()).string());
            throw new RuntimeException(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            logger.error("Error while executing request: " +  e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
