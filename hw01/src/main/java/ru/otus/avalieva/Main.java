package ru.otus.avalieva;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
                    messageSource.getMessage("error.testprocessor", new Object[]{e.getMessage()}) );
            e.printStackTrace();
        }
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageResource()  {
        ReloadableResourceBundleMessageSource messageResource =
                new ReloadableResourceBundleMessageSource();
        messageResource.setCacheSeconds(3600);
        messageResource.setBasenames("classpath:messages/messages");
        messageResource.setUseCodeAsDefaultMessage(true);
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }
}
