package com.avalieva.homework3.ru.otus.avalieva.testing;

import com.avalieva.homework3.ru.otus.avalieva.testing.impl.model.Question;

public interface Questionnaire {
    void printStartTestInfo();
    int askQuestion(final Question question, final int questionNumber);
    void printResult(int correctAnswers, int questionsAmount);
}
