package com.avalieva.homework3.ru.otus.avalieva.testing.impl.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class QuestionDto {
    @CsvBindByPosition(position = 0)
    private String question;

    @CsvBindByPosition(position = 1)
    private String answerA;

    @CsvBindByPosition(position = 2)
    private String answerB;

    @CsvBindByPosition(position = 3)
    private String answerC;

    @CsvBindByPosition(position = 4)
    private String answerD;

    @CsvBindByPosition(position = 5)
    private int correctAnswerNumber;
}
