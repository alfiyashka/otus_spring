package com.avalieva.homework3.ru.otus.avalieva.testing.impl;

import com.avalieva.homework3.ru.otus.avalieva.testing.IOService;
import com.avalieva.homework3.ru.otus.avalieva.testing.MessageService;
import com.avalieva.homework3.ru.otus.avalieva.testing.PersonalInfoCollector;
import org.springframework.stereotype.Service;

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

