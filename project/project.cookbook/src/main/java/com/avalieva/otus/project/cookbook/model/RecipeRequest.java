package com.avalieva.otus.project.cookbook.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeRequest {
    private String name;
    private String collection;
    private List<String> ingredients;
}

