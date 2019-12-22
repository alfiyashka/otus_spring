package ru.otus.avalieva.homework4.testing.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.avalieva.homework4.TestingApplication;
import ru.otus.avalieva.homework4.testing.IOService;
import ru.otus.avalieva.homework4.testing.MessageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = TestingApplication.class)
@SpringBootTest
public class PersonalInfoCollectorTest {

    @MockBean
    private IOService ioService;

    @MockBean
    private MessageService messageService;

    @Autowired
    private PersonalInfoCollectorImpl personalInfoCollector;

    @Test
    public void getFirstNameTest() {
        String askFirstNameMsg = "asking first name";
        when(messageService.getMessage("get.firstname"))
                .thenReturn(askFirstNameMsg);
        doNothing().when(ioService).outputData(askFirstNameMsg);
        String name = "SomeName";
        when(ioService.inputData()).thenReturn(name);

        var result = personalInfoCollector.getFirstName();
        assertEquals(name, result);

        verify(messageService, times(1)).getMessage("get.firstname");
        verify(ioService, times(1)).outputData(askFirstNameMsg);
        verify(ioService, times(1)).inputData();
    }


    @Test
    public void getLastNameTest() {
        String askLastNameMsg = "asking last name";
        when(messageService.getMessage("get.lastname"))
                .thenReturn(askLastNameMsg);
        doNothing().when(ioService).outputData(askLastNameMsg);
        String lastName = "SomeName";
        when(ioService.inputData()).thenReturn(lastName);

        var result = personalInfoCollector.getLastName();
        assertEquals(lastName, result);

        verify(messageService, times(1)).getMessage("get.lastname");
        verify(ioService, times(1)).outputData(askLastNameMsg);
        verify(ioService, times(1)).inputData();
    }

}
