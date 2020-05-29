package cookbook.common.config;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class EnumProperties {
     private static Properties properties = new Properties();

    private static Properties fetchProperties(){
        try (InputStream inputStream = EnumProperties.class.
                getClassLoader().getResourceAsStream("enum_ru_RU.properties")) {
            properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        }
        catch (IOException e) {
        }
        return properties;
    }

    public static Properties getProperties() {
        if (properties.isEmpty()) {
            fetchProperties();
        }
        return properties;
    }


}
