package ru.otus.avalieva.homework4.testing;

import ru.otus.avalieva.homework4.testing.impl.model.Question;

import java.util.List;

public interface QuestionsReader {
    List<Question> getQuestions();
}
