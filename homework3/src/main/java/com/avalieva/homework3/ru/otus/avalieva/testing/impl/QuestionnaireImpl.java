package com.avalieva.homework3.ru.otus.avalieva.testing.impl;

import com.avalieva.homework3.ru.otus.avalieva.testing.IOService;
import com.avalieva.homework3.ru.otus.avalieva.testing.MessageService;
import com.avalieva.homework3.ru.otus.avalieva.testing.PersonalInfoCollector;
import com.avalieva.homework3.ru.otus.avalieva.testing.Questionnaire;
import com.avalieva.homework3.ru.otus.avalieva.testing.impl.model.Question;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireImpl implements Questionnaire {
    private final IOService ioService;
    private final PersonalInfoCollector personalInfoCollector;
    private final MessageService messageService;

    public QuestionnaireImpl(final PersonalInfoCollector personalInfoCollector,
                             final IOService ioService,
                             final MessageService messageService) {
        this.personalInfoCollector = personalInfoCollector;
        this.ioService = ioService;
        this.messageService = messageService;
    }

    @Override
    public void printStartTestInfo() {
        ioService.outputData(
                messageService.getMessage("start.testing", firstName(), lastName())
        );
    }

    private String firstName() {
        return personalInfoCollector.getFirstName();
    }

    private String lastName() {
        return personalInfoCollector.getLastName();
    }

    @Override
    public int askQuestion(final Question question, final int questionNumber) {
        while (true) {
            ioService.outputData(
                    messageService.getMessage("question", questionNumber, question.getQuestion())
            );
            question.getAnswers()
                    .entrySet()
                    .forEach(it -> ioService.outputData(it.getValue()));
            ioService.outputData(
                    messageService.getMessage("answer.rule"));
            String data = ioService.inputData();
            try {
                int answer = Integer.parseInt(data);
                if (answer < 1 || answer > question.getAnswers().size()) {
                    ioService.outputData(
                            messageService.getMessage("error.incorrect.answer.number", answer)
                    );
                    continue;
                }
                return answer;
            } catch (Exception e) {
                ioService.outputData(
                        messageService.getMessage("error.incorrect.answer.not.number"));
            }
        }
    }

    @Override
    public void printResult(int correctAnswers, int questionsAmount) {
        ioService.outputData(
                messageService.getMessage("testing.result",
                        firstName(), lastName(), correctAnswers, questionsAmount)
        );
    }

}

