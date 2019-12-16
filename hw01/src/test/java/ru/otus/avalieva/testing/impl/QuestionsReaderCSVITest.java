package ru.otus.avalieva.testing.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.avalieva.testing.MessageService;
import ru.otus.avalieva.testing.QuestionsReader;
import ru.otus.avalieva.testing.impl.model.Question;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QuestionsReaderCSVITest {
    private final MessageService messageService = Mockito.mock(MessageService.class);

    public QuestionsReaderCSVITest() {

    }

    @Test
    public void getQuestionsFailed() {
        final String incorrectFilename = "filename";
        QuestionsReader questionsReader = new QuestionsCSVReaderImpl(incorrectFilename, messageService);
        String errorMsg = "Error";
        when(messageService.getMessage("error.cannot.get.questions", incorrectFilename))
                .thenReturn(errorMsg);

        var exc = Assertions.assertThrows(QuestionReaderException.class, () -> {
            questionsReader.getQuestions();
        });
        assertEquals(errorMsg, exc.getMessage());

        verify(messageService, times(1))
                .getMessage("error.cannot.get.questions", incorrectFilename);
    }

    private Question expectedQuestion() {
        Question question = new Question();
        question.setQuestion("Question?");
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "1) One");
        answers.put(2, "2) Two");
        answers.put(3, "3) Three");
        answers.put(4, "4) Four");
        question.setAnswers(answers);
        question.setCorrectAnswer(2);
        return question;
    }

    @Test
    public void getQuestionsTest() {
        final String filename = "test_questions.csv";
        QuestionsReader questionsReader = new QuestionsCSVReaderImpl(filename, messageService);

        var result = questionsReader.getQuestions();

        assertEquals(expectedQuestion(), result.get(0));
    }

    @Test
    public void getQuestionsTestFailedIncorrectFile() {
        final String filename = "incorrect.csv";
        QuestionsReader questionsReader = new QuestionsCSVReaderImpl(filename, messageService);

        String errorMsg = "Error";
        when(messageService.getMessage("error.cannot.get.questions", filename))
                .thenReturn(errorMsg);

        var exc = Assertions.assertThrows(QuestionReaderException.class, () -> {
            questionsReader.getQuestions();
        });
        assertEquals(errorMsg, exc.getMessage());

        verify(messageService, times(1))
                .getMessage("error.cannot.get.questions", filename);

    }
}
