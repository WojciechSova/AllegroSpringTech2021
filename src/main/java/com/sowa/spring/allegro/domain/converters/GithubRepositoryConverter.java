package com.sowa.spring.allegro.domain.converters;

import com.sowa.spring.allegro.domain.dtos.GithubRepositoryDto;
import com.sowa.spring.allegro.domain.model.GithubRepository;

public class GithubRepositoryConverter {

    public static GithubRepositoryDto convertGithubRepositoryToGithubRepositoryDto(GithubRepository githubRepository) {
        return new GithubRepositoryDto(githubRepository.getName(), githubRepository.getStars());
    }
}
