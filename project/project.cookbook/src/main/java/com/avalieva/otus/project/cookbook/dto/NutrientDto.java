package com.avalieva.otus.project.cookbook.dto;

import com.avalieva.otus.project.cookbook.model.ENutrient;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NutrientDto {
    private ENutrient nutrient;
    private float weight; //in grammes
}
