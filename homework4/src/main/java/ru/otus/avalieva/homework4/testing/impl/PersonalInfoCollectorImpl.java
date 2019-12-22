package ru.otus.avalieva.homework4.testing.impl;

import org.springframework.stereotype.Service;
import ru.otus.avalieva.homework4.testing.IOService;
import ru.otus.avalieva.homework4.testing.MessageService;
import ru.otus.avalieva.homework4.testing.PersonalInfoCollector;

@Service
public class PersonalInfoCollectorImpl implements PersonalInfoCollector {
    private final IOService ioService;
    private final MessageService messageService;
    private String firstName;
    private String lastName;

    public PersonalInfoCollectorImpl(final IOService ioService,
                                     final MessageService messageService)
    {
        this.ioService = ioService;
        this.messageService = messageService;
    }

    @Override
    public String getFirstName() {
        if (firstName == null) {
            ioService.outputData(
                    messageService.getMessage("get.firstname"));
            firstName = ioService.inputData();
        }
        return firstName;
    }

    @Override
    public String getLastName() {
        if (lastName == null) {
            ioService.outputData(
                    messageService.getMessage("get.lastname"));
            lastName = ioService.inputData();
        }
        return lastName;
    }

}
