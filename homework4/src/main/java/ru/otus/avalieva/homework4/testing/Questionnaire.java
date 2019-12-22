package ru.otus.avalieva.homework4.testing;

import ru.otus.avalieva.homework4.testing.impl.model.Question;

public interface Questionnaire {
    void printStartTestInfo();
    int askQuestion(final Question question, final int questionNumber);
    void printResult(int correctAnswers, int questionsAmount);
}
