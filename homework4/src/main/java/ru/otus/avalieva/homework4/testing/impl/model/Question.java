package ru.otus.avalieva.homework4.testing.impl.model;

import lombok.Data;

import java.util.Map;

@Data
public class Question {
    private String question;
    private Map<Integer, String> answers;
    private int correctAnswer;
}