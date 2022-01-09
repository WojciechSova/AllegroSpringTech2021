package com.sowa.spring.allegro.endpoints;

import com.sowa.spring.allegro.domain.converters.GithubRepositoryConverter;
import com.sowa.spring.allegro.domain.dtos.GithubRepositoryDto;
import com.sowa.spring.allegro.services.interfaces.GithubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/github")
@AllArgsConstructor
public class GithubEndpoint {

    private final GithubService githubService;

    @GetMapping(value = "/repositories/{githubUsername}", produces = "application/json")
    public ResponseEntity<List<GithubRepositoryDto>> getGithubRepositories(@PathVariable String githubUsername) {
        List<GithubRepositoryDto> githubRepositoriesDto = githubService.getRepositoriesFromGithubByUsername(githubUsername)
                .stream()
                .map(GithubRepositoryConverter::convertGithubRepositoryToGithubRepositoryDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(githubRepositoriesDto, OK);
    }

    @GetMapping(value = "/stars/{githubUsername}", produces = "text/plain")
    public ResponseEntity<String> getSumOfStars(@PathVariable String githubUsername) {
        return new ResponseEntity<>(githubService.getSumOfAllStarsInRepositories(githubUsername).toString(), OK);
    }

    @GetMapping(value = "/languages/{githubUsername}", produces = "application/json")
    public ResponseEntity<?> getLanguages(@PathVariable String githubUsername) {
        return new ResponseEntity<>(githubService.getLanguagesUsedByUser(githubUsername), OK);
    }
}
