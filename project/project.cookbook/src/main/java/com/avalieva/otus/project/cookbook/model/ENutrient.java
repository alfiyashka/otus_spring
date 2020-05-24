package com.avalieva.otus.project.cookbook.model;

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

    public static ENutrient eNutrient(String value){
        switch (value) {
            case "БЕЛОК": return ENutrient.PROTEIN;
            case "ЖИР": return ENutrient.FAT;
            case "УГЛЕВОД": return ENutrient.CARBOHYDRATE;
            default: throw new IllegalArgumentException(value);
        }
    }
}
