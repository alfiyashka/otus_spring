package ru.otus.avalieva;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.avalieva.testing.TestProcessor;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
        TestProcessor testProcessor = context.getBean(TestProcessor.class);
        try {
            testProcessor.test();
        }
        catch (Exception e) {
            System.out.println("The following error occurred:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
