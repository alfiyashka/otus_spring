package ru.avalieva.otus.recipe.recomendation.system.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientDto {
    private String ingredientName;
    private float weight;
}