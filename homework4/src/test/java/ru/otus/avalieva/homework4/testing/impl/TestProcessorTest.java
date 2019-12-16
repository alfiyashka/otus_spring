package ru.otus.avalieva.homework4.testing.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.avalieva.homework4.testing.MessageService;
import ru.otus.avalieva.homework4.testing.Questionnaire;
import ru.otus.avalieva.homework4.testing.QuestionsReader;
import ru.otus.avalieva.homework4.testing.TestProcessor;
import ru.otus.avalieva.homework4.testing.impl.exception.TestProcessorException;
import ru.otus.avalieva.homework4.testing.impl.model.Question;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class TestProcessorTest extends TestWithoutShell {
    @MockBean
    private Questionnaire questionnaire;

    @MockBean
    private QuestionsReader questionsСSVReader;

    @MockBean
    private MessageService messageService;

    private TestProcessor testProcessor;

    @Autowired
    public TestProcessorTest(TestProcessor testProcessor) {
        this.testProcessor = testProcessor;
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
        verify(messageService, never()).getMessage(anyString());
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
        verify(questionnaire, never()).printStartTestInfo();
    }

    @Test
    public void testingTestFailed_nullQuestionnaire() {
        testProcessor = new TestProcessorImpl(questionsСSVReader, null, messageService);
        when(messageService.getMessage("error.questionnaire.not.init")).thenReturn("error");

        Assertions.assertThrows(TestProcessorException.class, () -> {
            testProcessor.test();
        });
        verify(messageService).getMessage("error.questionnaire.not.init");
        verify(questionnaire, never()).printStartTestInfo();
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

        verify(questionnaire, never()).askQuestion(any(), anyInt());
        verify(questionnaire, never()).printResult(anyInt(), anyInt());
    }
}
