package com.sowa.spring.allegro.services.interfaces;

import com.sowa.spring.allegro.domain.model.GithubRepository;

import java.util.List;
import java.util.Map;

public interface GithubService {

    List<GithubRepository> getRepositoriesFromGithubByUsername(String githubUsername);

    Integer getSumOfAllStarsInRepositories(String githubUsername);

    Map<String, Integer> getLanguagesUsedByUser(String githubUsername);

}
