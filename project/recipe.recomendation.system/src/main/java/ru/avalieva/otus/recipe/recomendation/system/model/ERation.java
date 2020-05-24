package ru.avalieva.otus.recipe.recomendation.system.model;

public enum ERation {

    BREAKFAST("Завтрак"),
    LUNCH("Обед"),
    DINNER("Ужин");

    private final String value;
    ERation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
