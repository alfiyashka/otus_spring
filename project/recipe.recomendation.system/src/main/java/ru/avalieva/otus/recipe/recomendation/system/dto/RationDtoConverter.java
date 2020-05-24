package ru.avalieva.otus.recipe.recomendation.system.dto;

import ru.avalieva.otus.recipe.recomendation.system.model.ERation;

public class RationDtoConverter {
    public static RationDto convert(ERation ration) {
        return new RationDto(ration.name(), ration.getValue());
    }
}
