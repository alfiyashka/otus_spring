package ru.otus.avalieva.testing.impl.dto;

import ru.otus.avalieva.testing.impl.model.Question;

import java.util.HashMap;
import java.util.Map;

public class QuestionDtoConverter {
    public static Question convert (QuestionDto questionDto) {
        if (questionDto == null){
            return null;
        }
        Question question = new Question();
        question.setQuestion(questionDto.getQuestion());
        question.setCorrectAnswer(questionDto.getCorrectAnswerNumber());
        Map<Integer, String> answers = new HashMap();
        answers.put(1, questionDto.getAnswerA());
        answers.put(2, questionDto.getAnswerB());
        answers.put(3, questionDto.getAnswerC());
        answers.put(4, questionDto.getAnswerD());
        question.setAnswers(answers);
        return question;
    }
}

