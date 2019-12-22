package ru.otus.avalieva.homework4.testing.impl.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.avalieva.homework4.testing.TestProcessor;

@ShellComponent
public class TestingShell {

    private final TestProcessor testProcessor;

    public TestingShell(TestProcessor testProcessor) {
        this.testProcessor = testProcessor;
    }

    @ShellMethod(key = "start_test", value = "start test")
    public void startTest() {
        testProcessor.test();
    }
}
