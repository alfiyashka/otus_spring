package ru.otus.avalieva.testing;

import ru.otus.avalieva.testing.impl.model.Question;

import java.util.List;

public interface QuestionsCSVReader {
    List<Question> getQuestions();
}
