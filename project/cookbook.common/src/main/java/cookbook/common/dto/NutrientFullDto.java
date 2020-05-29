package cookbook.common.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NutrientFullDto {
    private String nutrient;
    private String nutrientValue;
    private float weight;
}
