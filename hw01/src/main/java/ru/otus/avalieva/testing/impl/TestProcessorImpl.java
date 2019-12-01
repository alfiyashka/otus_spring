package ru.otus.avalieva.testing.impl;

import ru.otus.avalieva.testing.Questionnaire;
import ru.otus.avalieva.testing.TestProcessor;
import ru.otus.avalieva.testing.QuestionsReader;
import ru.otus.avalieva.testing.impl.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestProcessorImpl implements TestProcessor {
    private final QuestionsReader questionsReader;

    private final Questionnaire questionnaire;

    public TestProcessorImpl(QuestionsReader reader,
                             Questionnaire questionnaire) {
        this.questionsReader = reader;
        this.questionnaire = questionnaire;
    }

    @Override
    public void test() {
        if (questionsReader == null) {
            throw new TestProcessorException("The question reader is not initialized");
        }
        if (questionnaire == null) {
            throw new TestProcessorException("The questionnaire is not initialized");
        }
        questionnaire.printStartTestInfo();
        List<Question> questions = questionsReader.getQuestions();
        Map<Integer, Boolean> answers = askQuestions(questions);
        questionnaire.printResult(calculateCorrectAnswers(answers), answers.size());
    }

    private Map<Integer, Boolean> askQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            throw new TestProcessorException("Missing list of questions");
        }
        Map<Integer, Boolean> answers = new HashMap<>();
        int questionNumber = 1;

        for (Question question : questions) {
            int iAnswer = questionnaire.askQuestion(question, questionNumber);
            answers.put(questionNumber, iAnswer == question.getCorrectAnswer());
            questionNumber++;
        }
        return answers;
    }

    private int calculateCorrectAnswers(Map<Integer, Boolean> answers) {
        return (int)answers
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .count();
    }

}
