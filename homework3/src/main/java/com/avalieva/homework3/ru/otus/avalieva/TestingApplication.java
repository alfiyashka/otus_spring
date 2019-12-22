package com.avalieva.homework3.ru.otus.avalieva;

import com.avalieva.homework3.ru.otus.avalieva.testing.IOService;
import com.avalieva.homework3.ru.otus.avalieva.testing.MessageService;
import com.avalieva.homework3.ru.otus.avalieva.testing.TestProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestingApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(TestingApplication.class, args);
        IOService ioService = context.getBean(IOService.class);
        MessageService messageSource = context.getBean(MessageService.class);
        TestProcessor testProcessor = context.getBean(TestProcessor.class);
        try {
            testProcessor.test();
        } catch (Exception e) {
            ioService.outputData(
                    messageSource.getMessage("error.testprocessor", e.getMessage()));
            e.printStackTrace();
        }
    }

}
