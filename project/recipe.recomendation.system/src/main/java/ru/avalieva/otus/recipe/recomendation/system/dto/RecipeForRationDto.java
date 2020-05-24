package ru.avalieva.otus.recipe.recomendation.system.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeForRationDto {
    RationDto ration;
    List<RecipeDtoJson> recipes;
}
