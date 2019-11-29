package ru.otus.avalieva.testing.impl;

import ru.otus.avalieva.testing.Questionnaire;
import ru.otus.avalieva.testing.impl.model.Question;

import java.util.Scanner;

public class QuestionnaireImpl implements Questionnaire {
    private String firstName;
    private String lastName;
    private final Scanner scanner;

    public QuestionnaireImpl()
    {
        scanner = new Scanner(System.in);
    }

    private void getFirstName() {
        System.out.println("Enter your first name:");
        firstName = scanner.nextLine();
    }

    private void getLastName() {
        System.out.println("Enter your last name:");
        lastName = scanner.nextLine();
    }

    @Override
    public void printStartTestInfo() {
        getFirstName();
        getLastName();
        System.out.println(
                String.format("%s %s, the testing starts, give the answer in the form of numbers.",
                        firstName, lastName));
    }

    @Override
    public int askQuestion(final Question question, final int questionNumber) {
        while(true) {
            System.out.println(String.format("%d. %s", questionNumber, question.getQuestion()));
            question.getAnswers()
                    .entrySet()
                    .stream()
                    .forEach(it -> System.out.println(it.getValue()));
            System.out.println("Please, give the answer in the form of numbers:");
            String data = scanner.nextLine();
            try {
                int answer = Integer.parseInt(data);
                if (answer < 1 || answer > 4) {
                    System.out.println("Incorrect input: the answer with number " + answer +" does not exist. Try again ");
                    continue;
                }
                return answer;
            }
            catch (Exception e) {
                System.out.println("Incorrect input: cannot get number, try again ");
            }
        }
    }

    @Override
    public void printResult(int correctAnswers, int questionsAmount) {
        System.out.println(
                String.format("%s %s, your result: correct %d from %d", firstName, lastName,
                        correctAnswers, questionsAmount)
        );
    }

}

