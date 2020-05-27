package cookbook.common.dto;
import cookbook.common.config.EnumProperties;
import cookbook.common.model.ERation;

public class RationDtoConverter {


    public static RationDto convert(ERation ration) {
        String value = EnumProperties.getProperties().getProperty(String.format("ERation.%s", ration.name()));
        return new RationDto(ration.name(),
                value);
    }
}