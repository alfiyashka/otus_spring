package ru.avalieva.otus.recipe.recomendation.system.model;

public enum ERecipeType {
    BREAKFAST("ЗАВТРАК"),
    SALAD("САЛАТ"),
    SOUP("СУП"),
    MAIN_COURSE("ВТОРОЕ БЛЮДО"),
    SIDE_DISHES("ГАРНИР"),
    APPETIZER("ЗАКУСКА"),
    DESSERTS("ДИСЕРТ");

    private final String value;
    ERecipeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

