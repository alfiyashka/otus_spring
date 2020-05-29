package cookbook.common.dto;

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
