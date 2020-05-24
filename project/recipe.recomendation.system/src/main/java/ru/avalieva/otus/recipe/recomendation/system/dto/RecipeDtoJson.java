package ru.avalieva.otus.recipe.recomendation.system.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeDtoJson {
    private String name;
    private String type;
    private int totalCalorie;
    private String description;

    List<IngredientDto> ingredients;
    List<NutrientDto> nutrients;
}

