package ru.otus.avalieva.homework4.testing.impl;

import org.springframework.stereotype.Service;
import ru.otus.avalieva.homework4.testing.IOService;
import ru.otus.avalieva.homework4.testing.MessageService;
import ru.otus.avalieva.homework4.testing.PersonalInfoCollector;
import ru.otus.avalieva.homework4.testing.Questionnaire;
import ru.otus.avalieva.homework4.testing.impl.model.Question;

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

