package ru.avalieva.otus.recipe.recomendation.system.dto;

import lombok.*;
import ru.avalieva.otus.recipe.recomendation.system.model.ENutrient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NutrientDto {
    private ENutrient nutrient;
    private float weight; //in grammes
}
