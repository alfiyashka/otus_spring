package ru.avalieva.otus.recipe.recomendation.system.model;

public enum ERationStrategy {
    RANDOM("Случайный выбор"),
    VEGAN("Веганская диета"),
    DIET_SOUP("Суповая Диета");

    private final String value;
    ERationStrategy(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }


}

