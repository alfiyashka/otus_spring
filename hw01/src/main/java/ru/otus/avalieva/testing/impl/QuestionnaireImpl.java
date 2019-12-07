package ru.otus.avalieva.testing.impl;

import ru.otus.avalieva.testing.IOService;
import ru.otus.avalieva.testing.PersonalInfoCollector;
import ru.otus.avalieva.testing.Questionnaire;
import ru.otus.avalieva.testing.impl.model.Question;


public class QuestionnaireImpl implements Questionnaire {
    private final IOService ioService;
    private final PersonalInfoCollector personalInfoCollector;

    private String firstName;
    private String lastName;


    public QuestionnaireImpl(PersonalInfoCollector personalInfoCollector,
                             IOService ioService)
    {
        this.personalInfoCollector = personalInfoCollector;
        this.ioService = ioService;
    }

    @Override
    public void printStartTestInfo() {
        firstName = personalInfoCollector.getFirstName();
        lastName = personalInfoCollector.getLastName();
        ioService.outputData(
                String.format("%s %s, the testing starts, give the answer in the form of numbers.",
                        firstName, lastName));
    }

    @Override
    public int askQuestion(final Question question, final int questionNumber) {
        while(true) {
            ioService.outputData(String.format("%d. %s", questionNumber, question.getQuestion()));
            question.getAnswers()
                    .entrySet()
                    .stream()
                    .forEach(it -> ioService.outputData(it.getValue()));
            ioService.outputData("Please, give the answer in the form of numbers:");
            String data = ioService.inputData();
            try {
                int answer = Integer.parseInt(data);
                if (answer < 1 || answer > 4) {
                    ioService.outputData("Incorrect input: the answer with number " + answer +" does not exist. Try again ");
                    continue;
                }
                return answer;
            }
            catch (Exception e) {
                ioService.outputData("Incorrect input: cannot get number, try again ");
            }
        }
    }

    @Override
    public void printResult(int correctAnswers, int questionsAmount) {
        ioService.outputData(
                String.format("%s %s, your result: correct %d from %d", firstName, lastName,
                        correctAnswers, questionsAmount)
        );
    }

}

