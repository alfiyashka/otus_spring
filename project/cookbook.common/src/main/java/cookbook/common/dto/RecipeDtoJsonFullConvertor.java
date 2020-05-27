package cookbook.common.dto;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeDtoJsonFullConvertor {
    public static RecipeDtoJsonFull convert(RecipeDtoJson recipeDtoJson) {
        List<NutrientFullDto> nutrientFullDtos =
                recipeDtoJson.getNutrients() != null
                        ? recipeDtoJson.getNutrients()
                          .stream()
                          .filter(it -> it != null)
                          .map(NutrientFullDtoConvertor::convert)
                          .collect(Collectors.toList())
                        : null;

        return new RecipeDtoJsonFull(recipeDtoJson.getName(), recipeDtoJson.getType(), recipeDtoJson.getTotalCalorie(),
                recipeDtoJson.getDescription(), recipeDtoJson.getIngredients(), nutrientFullDtos);
    }
}
