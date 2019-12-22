package ru.otus.avalieva.homework4.testing.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import ru.otus.avalieva.homework4.testing.MessageService;
import ru.otus.avalieva.homework4.testing.QuestionsReader;
import ru.otus.avalieva.homework4.testing.impl.configuration.LocaleSettings;
import ru.otus.avalieva.homework4.testing.impl.dto.QuestionDto;
import ru.otus.avalieva.homework4.testing.impl.dto.QuestionDtoConverter;
import ru.otus.avalieva.homework4.testing.impl.exception.QuestionReaderException;
import ru.otus.avalieva.homework4.testing.impl.model.Question;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionsCSVReaderImpl implements QuestionsReader {
    private final String filename;
    private final MessageService messageService;

    public QuestionsCSVReaderImpl(LocaleSettings localeSettings,
                                  final MessageService messageService){
        this.filename = localeSettings.getFilename();
        this.messageService = messageService;

    }

    @Override
    public List<Question> getQuestions() {
        try (InputStream inputStream = QuestionsCSVReaderImpl.class.
                getClassLoader().getResourceAsStream(filename);
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
                    messageService.getMessage("error.cannot.get.questions", filename),
                    e);
        }
    }
}
