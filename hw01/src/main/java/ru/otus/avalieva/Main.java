package ru.otus.avalieva;

import org.springframework.context.annotation.*;
import ru.otus.avalieva.testing.IOService;
import ru.otus.avalieva.testing.MessageService;
import ru.otus.avalieva.testing.TestProcessor;


@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);
        IOService ioService = context.getBean(IOService.class);
        MessageService messageSource = context.getBean(MessageService.class);
        TestProcessor testProcessor = context.getBean(TestProcessor.class);
        try {
            testProcessor.test();
        }
        catch (Exception e) {
            ioService.outputData(
                    messageSource.getMessage("error.testprocessor", e.getMessage()));
            e.printStackTrace();
        }
    }


}
