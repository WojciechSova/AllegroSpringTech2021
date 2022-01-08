package com.sowa.spring.allegro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GithubRepositoryDto {

    private String name;
    private Integer stars;
}
