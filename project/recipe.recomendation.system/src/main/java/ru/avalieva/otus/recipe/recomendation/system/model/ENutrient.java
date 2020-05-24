package ru.avalieva.otus.recipe.recomendation.system.model;

public enum ENutrient {
    PROTEIN("БЕЛОК"),
    FAT("ЖИР"),
    CARBOHYDRATE("УГЛЕВОД");


    private final String value;
    ENutrient(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
