package cookbook.common.dto;

import cookbook.common.config.EnumProperties;
import cookbook.common.model.ERationStrategy;

public class RationStrategyDtoConverter {
    public static RationStrategyDto convert(ERationStrategy rationStrategy) {
        String value = EnumProperties.getProperties().getProperty(String.format("ERationStrategy.%s", rationStrategy.name()));
        return new RationStrategyDto(rationStrategy.name(), value);
    }
}

