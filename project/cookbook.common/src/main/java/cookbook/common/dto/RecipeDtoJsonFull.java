package cookbook.common.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeDtoJsonFull {
    private String name;
    private String type;
    private int totalCalorie;
    private String description;

    private List<IngredientDto> ingredients;
    private List<NutrientFullDto> nutrients;

}
