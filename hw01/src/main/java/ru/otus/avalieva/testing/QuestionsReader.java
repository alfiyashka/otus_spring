package ru.otus.avalieva.testing;

import ru.otus.avalieva.testing.impl.model.Question;

import java.util.List;

public interface QuestionsReader {
    List<Question> getQuestions();
}
