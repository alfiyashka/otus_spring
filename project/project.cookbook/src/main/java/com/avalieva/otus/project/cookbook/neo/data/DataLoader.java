package com.avalieva.otus.project.cookbook.neo.data;

import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import com.avalieva.otus.project.cookbook.service.CookbookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader {
    public final CookbookService cookbookService;


    public void createTestData() throws Exception {
        cookbookService.clearNeo4jData();
        ObjectMapper mapper = new ObjectMapper();
        try(InputStream is = DataLoader.class.getClassLoader().getResourceAsStream("recipes.json")) {
            List<RecipeDtoJson> recipes = mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class));
            recipes.forEach(cookbookService::addRecipe);
        }
    }
}
