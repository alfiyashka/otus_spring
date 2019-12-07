package ru.otus.avalieva.testing.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.avalieva.testing.MessageService;
import ru.otus.avalieva.testing.Questionnaire;
import ru.otus.avalieva.testing.QuestionsReader;
import ru.otus.avalieva.testing.impl.model.Question;
import ru.otus.avalieva.testing.TestProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TestProcessorTest {

    private final Questionnaire questionnaire = Mockito.mock(Questionnaire.class);
    private final QuestionsReader questionsСSVReader = Mockito.mock(QuestionsReader.class);
    private final MessageService messageService = Mockito.mock(MessageServiceImpl.class);

    private TestProcessor testProcessor;

    @BeforeEach
    public void init() {
        testProcessor = new TestProcessorImpl(questionsСSVReader, questionnaire, messageService);
    }

    private List<Question> generateQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Question iQuestion = new Question();
            iQuestion.setCorrectAnswer(1);
            questions.add(iQuestion);
        }

        return questions;
    }

    @Test
    public void testingTest() {
        doNothing().when(questionnaire).printStartTestInfo();
        List<Question> questions = generateQuestions();

        when(questionsСSVReader.getQuestions()).thenReturn(questions);
        int questionNumber = 1;
        for (Question question : questions) {
            when(questionnaire.askQuestion(question, questionNumber))
                    .thenReturn((questionNumber % 2 == 0) ? 1 : 3);
            questionNumber++;
        }

        testProcessor.test();

        verify(questionnaire, times(1)).printStartTestInfo();
        verify(questionsСSVReader, times(1)).getQuestions();
        verify(questionnaire, times(questions.size())).askQuestion(any(), anyInt());

        int correctResults = questions.size() / 2;
        int questionsAmount = questions.size();
        verify(questionnaire, times(1)).printResult(correctResults, questionsAmount);
        verify(messageService, times(0)).getMessage(anyString());
    }

    @Test
    public void testingTestFailed_nullCSVReader() {
        testProcessor = new TestProcessorImpl(null, questionnaire, messageService);
        when(messageService.getMessage("error.question.reader.not.init")).thenReturn("error");

        var exc = Assertions.assertThrows(TestProcessorException.class, () -> {
            testProcessor.test();
        });
        exc.getMessage();

        verify(messageService).getMessage("error.question.reader.not.init");
        verify(questionnaire, times(0)).printStartTestInfo();
    }

    @Test
    public void testingTestFailed_nullQuestionnaire() {
        testProcessor = new TestProcessorImpl(questionsСSVReader, null, messageService);
        when(messageService.getMessage("error.questionnaire.not.init")).thenReturn("error");

        Assertions.assertThrows(TestProcessorException.class, () -> {
            testProcessor.test();
        });
        verify(messageService).getMessage("error.questionnaire.not.init");
        verify(questionnaire, times(0)).printStartTestInfo();
    }

    @Test
    public void testingTestFailed_noQuestions() {
        doNothing().when(questionnaire).printStartTestInfo();
        when(messageService.getMessage("error.no.questions")).thenReturn("error");
        when(questionsСSVReader.getQuestions()).thenReturn(new ArrayList<>());

        Assertions.assertThrows(TestProcessorException.class, () -> { testProcessor.test(); });

        verify(messageService, times(1)).getMessage("error.no.questions");
        verify(questionnaire, times(1)).printStartTestInfo();
        verify(questionsСSVReader, times(1)).getQuestions();

        verify(questionnaire, times(0)).askQuestion(any(), anyInt());
        verify(questionnaire, times(0)).printResult(anyInt(), anyInt());
    }
}

