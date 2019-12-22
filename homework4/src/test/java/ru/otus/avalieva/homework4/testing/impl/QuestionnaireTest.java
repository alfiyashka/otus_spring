package ru.otus.avalieva.homework4.testing.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.avalieva.homework4.TestingApplication;
import ru.otus.avalieva.homework4.testing.IOService;
import ru.otus.avalieva.homework4.testing.MessageService;
import ru.otus.avalieva.homework4.testing.PersonalInfoCollector;
import ru.otus.avalieva.homework4.testing.Questionnaire;
import ru.otus.avalieva.homework4.testing.impl.model.Question;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = TestingApplication.class)
@SpringBootTest
public class QuestionnaireTest {

    @MockBean
    protected IOService ioService;

    @MockBean
    private PersonalInfoCollector personalInfoCollector;

    @MockBean
    private MessageService messageService;

    @Autowired
    private Questionnaire questionnaire;

    @Test
    public void printStartTestInfoTest() {
        String firstName = "Name";
        String lastName = "LastName";
        when(personalInfoCollector.getFirstName()).thenReturn(firstName);
        when(personalInfoCollector.getLastName()).thenReturn(lastName);

        String startTestingMsg = "Start";
        when(messageService.getMessage("start.testing", firstName, lastName))
                .thenReturn(startTestingMsg);
        doNothing().when(ioService).outputData(startTestingMsg);

        questionnaire.printStartTestInfo();

        verify(personalInfoCollector, times(1)).getFirstName();
        verify(personalInfoCollector, times(1)).getLastName();

        verify(messageService, times(1))
                .getMessage("start.testing", firstName, lastName);
        verify(ioService, times(1)).outputData(startTestingMsg);
    }

    @Test
    public void printResultTest() {
        int correctAnswers = 4;
        int answers = 5;
        String resultMsg = "result";

        String firstName = "Name";
        String lastName = "LastName";
        when(personalInfoCollector.getFirstName()).thenReturn(firstName);
        when(personalInfoCollector.getLastName()).thenReturn(lastName);

        when(messageService.getMessage("testing.result",
                firstName, lastName, correctAnswers, answers))
                .thenReturn(resultMsg);
        doNothing().when(ioService).outputData(resultMsg);

        questionnaire.printResult(correctAnswers, answers);

        verify(messageService, times(1)).getMessage("testing.result",
                firstName, lastName, correctAnswers, answers);
        verify(ioService, times(1)).outputData(resultMsg);
    }

    private Question question() {
        Question question = new Question();
        question.setQuestion("Question");
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "answer1");
        answers.put(2, "answer2");
        question.setAnswers(answers);
        return question;
    }

    @Test
    public void askQuestionTest() {
        Question question = question();
        int questionNumber = 1;

        String questionMsg = "Question";
        when(messageService.getMessage("question", questionNumber, question.getQuestion()))
                .thenReturn(questionMsg);
        doNothing().when(ioService).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            doNothing().when(ioService).outputData(answer.getValue());
        }
        String answerRuleMsg = "Rule";
        when(messageService.getMessage("answer.rule")).thenReturn(answerRuleMsg);
        doNothing().when(ioService).outputData(answerRuleMsg);
        int givenAnswer = 1;
        when(ioService.inputData()).thenReturn(Integer.toString(givenAnswer));

        int result = questionnaire.askQuestion(question, questionNumber);
        assertEquals(givenAnswer, result);

        verify(messageService, times(1))
                .getMessage("question", questionNumber, question.getQuestion());
        verify(ioService, times(1)).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            verify(ioService, times(1)).outputData(answer.getValue());
        }
        verify(messageService, times(1)).getMessage("answer.rule");
        verify(ioService, times(1)).outputData(answerRuleMsg);
        verify(ioService, times(1)).inputData();
    }

    @Test
    public void askQuestionTestIncorrectAnswerNumber() {
        Question question = question();
        int questionNumber = 1;

        String questionMsg = "Question";
        when(messageService.getMessage("question", questionNumber, question.getQuestion()))
                .thenReturn(questionMsg);
        doNothing().when(ioService).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            doNothing().when(ioService).outputData(answer.getValue());
        }
        String answerRuleMsg = "Rule";
        when(messageService.getMessage("answer.rule")).thenReturn(answerRuleMsg);
        doNothing().when(ioService).outputData(answerRuleMsg);
        Integer givenIncorrectNumberAnswer = 5;
        int givenAnswer = 1;
        when(ioService.inputData()).thenReturn(givenIncorrectNumberAnswer.toString())
                .thenReturn(Integer.toString(givenAnswer));

        String incorrectNumberAnswerMsg = "Incorrect answer";
        when(messageService.getMessage("error.incorrect.answer.number", givenIncorrectNumberAnswer))
                .thenReturn(incorrectNumberAnswerMsg);
        doNothing().when(ioService).outputData(incorrectNumberAnswerMsg);

        int result = questionnaire.askQuestion(question, questionNumber);
        assertEquals(givenAnswer, result);

        verify(messageService, times(2))
                .getMessage("question", questionNumber, question.getQuestion());
        verify(ioService, times(2)).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            verify(ioService, times(2)).outputData(answer.getValue());
        }
        verify(messageService, times(2)).getMessage("answer.rule");
        verify(ioService, times(2)).outputData(answerRuleMsg);
        verify(ioService, times(2)).inputData();
        verify(messageService, times(1))
                .getMessage("error.incorrect.answer.number", givenIncorrectNumberAnswer);
        verify(ioService, times(1)).outputData(incorrectNumberAnswerMsg);
    }

    @Test
    public void askQuestionTestNonNumberAnswer() {
        Question question = question();
        int questionNumber = 1;

        String questionMsg = "Question";
        when(messageService.getMessage("question", questionNumber, question.getQuestion()))
                .thenReturn(questionMsg);
        doNothing().when(ioService).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            doNothing().when(ioService).outputData(answer.getValue());
        }
        String answerRuleMsg = "Rule";
        when(messageService.getMessage("answer.rule")).thenReturn(answerRuleMsg);
        doNothing().when(ioService).outputData(answerRuleMsg);
        String givenNonNumberAnswer = "dsd";
        int givenAnswer = 1;
        when(ioService.inputData()).thenReturn(givenNonNumberAnswer)
                .thenReturn(Integer.toString(givenAnswer));

        String notNumberAnswerMsg = "Not number";
        when(messageService.getMessage("error.incorrect.answer.not.number"))
                .thenReturn(notNumberAnswerMsg);
        doNothing().when(ioService).outputData(notNumberAnswerMsg);

        var result = questionnaire.askQuestion(question, questionNumber);
        assertEquals(givenAnswer, result);

        verify(messageService, times(2))
                .getMessage("question", questionNumber, question.getQuestion());
        verify(ioService, times(2)).outputData(questionMsg);
        for (Map.Entry<Integer, String> answer : question.getAnswers().entrySet()) {
            verify(ioService, times(2)).outputData(answer.getValue());
        }
        verify(messageService, times(2)).getMessage("answer.rule");
        verify(ioService, times(2)).outputData(answerRuleMsg);
        verify(ioService, times(2)).inputData();
        verify(messageService, times(1))
                .getMessage("error.incorrect.answer.not.number");
        verify(ioService, times(1)).outputData(notNumberAnswerMsg);
    }
}
