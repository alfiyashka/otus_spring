package cookbook.common.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeForRationDto {
    RationDto ration;
    List<RecipeDtoJsonFull> recipes;
}
