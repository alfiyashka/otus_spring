package ru.otus.avalieva.testing.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.avalieva.testing.Questionnaire;
import ru.otus.avalieva.testing.QuestionsCSVReader;
import ru.otus.avalieva.testing.impl.model.Question;
import ru.otus.avalieva.testing.TestProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TestProcessorTest {

    private final Questionnaire questionnaire = Mockito.mock(Questionnaire.class);
    private final QuestionsCSVReader questionsСSVReader = Mockito.mock(QuestionsCSVReader.class);

    private TestProcessor testProcessor;

    @BeforeEach
    public void init()
    {
        testProcessor = new TestProcessorImpl(questionsСSVReader, questionnaire);
    }

    public List<Question> generateQuestions() {
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
        for (Question question: questions) {
            when(questionnaire.askQuestion(question, questionNumber))
                    .thenReturn((questionNumber % 2 == 0) ? 1 : 3);
            questionNumber ++;
        }

        testProcessor.test();

        verify(questionnaire, times(1)).printStartTestInfo();
        verify(questionsСSVReader, times(1)).getQuestions();
        verify(questionnaire, times(questions.size())).askQuestion(any(), anyInt());

        int correctResults = questions.size() / 2;
        int questionsAmount = questions.size();
        verify(questionnaire, times(1)).printResult(correctResults, questionsAmount);
    }

    @Test
    public void testingTestFailed_nullCSVReader() {
        testProcessor = new TestProcessorImpl(null, questionnaire);

        Assertions.assertThrows(RuntimeException.class, () -> {
            testProcessor.test();
        });

        verify(questionnaire, times(0)).printStartTestInfo();
    }

    @Test
    public void testingTestFailed_nullQuestionnaire() {
        testProcessor = new TestProcessorImpl(questionsСSVReader, null);

        Assertions.assertThrows(RuntimeException.class, () -> {
            testProcessor.test();
        });
        verify(questionnaire, times(0)).printStartTestInfo();
    }

    @Test
    public void testingTestFailed_noQuestions() {
        doNothing().when(questionnaire).printStartTestInfo();

        when(questionsСSVReader.getQuestions()).thenReturn(new ArrayList());

        Assertions.assertThrows(RuntimeException.class, () -> {
            testProcessor.test();
        });

        verify(questionnaire, times(1)).printStartTestInfo();
        verify(questionsСSVReader, times(1)).getQuestions();

        verify(questionnaire, times(0)).askQuestion(any(), anyInt());
        verify(questionnaire, times(0)).printResult(anyInt(), anyInt());
    }
}

