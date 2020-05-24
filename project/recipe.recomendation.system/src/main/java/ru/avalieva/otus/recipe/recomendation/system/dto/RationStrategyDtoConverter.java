package ru.avalieva.otus.recipe.recomendation.system.dto;

import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;

public class RationStrategyDtoConverter {
    public static RationStrategyDto convert(ERationStrategy rationStrategy) {
        return new RationStrategyDto(rationStrategy.name(), rationStrategy.getValue());
    }
}
