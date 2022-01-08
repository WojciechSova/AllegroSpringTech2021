package com.sowa.spring.allegro.services.interfaces;

import com.sowa.spring.allegro.domain.model.GithubRepository;

import java.util.List;

public interface GithubService {

    List<GithubRepository> getRepositoriesFromGithubByUsername(String githubUsername);

    Integer getSumOfAllStarsInRepositories(String githubUsername);

}
