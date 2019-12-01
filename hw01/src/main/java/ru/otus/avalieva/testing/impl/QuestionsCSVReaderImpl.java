package ru.otus.avalieva.testing.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.avalieva.testing.QuestionsReader;
import ru.otus.avalieva.testing.impl.dto.QuestionDto;
import ru.otus.avalieva.testing.impl.dto.QuestionDtoConverter;
import ru.otus.avalieva.testing.impl.model.Question;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionsCSVReaderImpl implements QuestionsReader {
    private final String filename;

    public QuestionsCSVReaderImpl(final String filename){
        this.filename = filename;
    }

    @Override
    public List<Question> getQuestions() {
        try (InputStream inputStream = QuestionsCSVReaderImpl.class.getClassLoader().getResourceAsStream(filename);
             final Reader reader = new InputStreamReader(inputStream)){

            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(QuestionDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();

            return csvToBean
                    .parse()
                    .stream()
                    .map(QuestionDtoConverter::convert)
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            throw new QuestionReaderException(
                    String.format("Unable to create a list of questions. Error: %s", filename),
                    e);
        }
    }
}
