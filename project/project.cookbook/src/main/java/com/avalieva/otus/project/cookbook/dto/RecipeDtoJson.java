package com.avalieva.otus.project.cookbook.dto;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class RecipeDtoJson {
    private String name;
    private String type;
    private int totalCalorie;
    private String description;

    List<IngredientDto> ingredients;
    List<NutrientDto> nutrients;

    @JsonIgnore
    public Recipe getRecipe() {
        ObjectId id = new ObjectId();
        return new Recipe(id.toHexString(), name, totalCalorie, description);
    }

    @JsonIgnore
    public RecipeTypeDto getRecipeType() {
        return new RecipeTypeDto(type.toUpperCase());
    }
}
