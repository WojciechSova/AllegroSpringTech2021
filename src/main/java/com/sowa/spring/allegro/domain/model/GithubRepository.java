package com.sowa.spring.allegro.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepository {

    @JsonProperty("name")
    private String name;
    @JsonProperty("stargazers_count")
    private Integer stars;

}
