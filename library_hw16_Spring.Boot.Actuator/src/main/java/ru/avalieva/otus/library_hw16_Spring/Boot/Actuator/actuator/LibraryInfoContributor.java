package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LibraryInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String,String> data= new HashMap<>();
        data.put("app", "Library Application");
        data.put("build.version", "1");
        builder.withDetail("AppInfo", data);
    }

}