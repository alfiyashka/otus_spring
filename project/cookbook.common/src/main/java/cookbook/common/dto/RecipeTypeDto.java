package cookbook.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RecipeTypeDto {
    private String recipeType;
    private String recipeTypeValue;
}
