package cookbook.common.dto;

import cookbook.common.config.EnumProperties;

public class NutrientFullDtoConvertor {
    public static NutrientFullDto convert(NutrientDto nutrientDto) {
        String nutrientValue = EnumProperties.getProperties().getProperty(String.format("ENutrient.%s", nutrientDto.getNutrient()));
        return new NutrientFullDto(nutrientDto.getNutrient().name(), nutrientValue, nutrientDto.getWeight());
    }
}
