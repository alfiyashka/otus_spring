package ru.otus.avalieva.testing.impl;

import ru.otus.avalieva.testing.IOService;
import ru.otus.avalieva.testing.PersonalInfoCollector;

public class PersonalInfoCollectorImpl implements PersonalInfoCollector {
    private final IOService ioService;

    public PersonalInfoCollectorImpl(final IOService ioService)
    {
        this.ioService = ioService;
    }

    @Override
    public String getFirstName() {
        ioService.outputData("Enter your first name:");
        return ioService.inputData();
    }

    @Override
    public String getLastName() {
        ioService.outputData("Enter your last name:");
        return ioService.inputData();
    }

}
