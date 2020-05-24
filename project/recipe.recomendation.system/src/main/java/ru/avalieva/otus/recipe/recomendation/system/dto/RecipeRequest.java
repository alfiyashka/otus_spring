package ru.avalieva.otus.recipe.recomendation.system.dto;

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
