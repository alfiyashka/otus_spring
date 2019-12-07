package ru.otus.avalieva.testing.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.avalieva.testing.IOService;
import ru.otus.avalieva.testing.MessageService;
import ru.otus.avalieva.testing.PersonalInfoCollector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PersonalInfoCollectorTest {
    private final IOService ioService = Mockito.mock(IOService.class);
    private final MessageService messageService = Mockito.mock(MessageServiceImpl.class);

    private PersonalInfoCollector personalInfoCollector;

    @BeforeEach
    public void init() {
        personalInfoCollector = new PersonalInfoCollectorImpl(ioService, messageService);
    }

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
