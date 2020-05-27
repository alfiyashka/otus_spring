package cookbook.common.dto;

import cookbook.common.model.ENutrient;
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
