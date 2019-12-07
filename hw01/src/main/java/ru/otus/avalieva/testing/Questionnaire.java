package ru.otus.avalieva.testing;

import ru.otus.avalieva.testing.impl.model.Question;

public interface Questionnaire {
    void printStartTestInfo();
    int askQuestion(final Question question, final int questionNumber);
    void printResult(int correctAnswers, int questionsAmount);
}
