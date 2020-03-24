package ru.avalieva.otus.hw15spring.integration.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.hw15spring.integration.service.IOService;

import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService {
    private final Scanner scanner;

    public IOServiceImpl() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String inputData() {
        return scanner.nextLine();
    }

    @Override
    public void outputData(String data) {
        System.out.println(data);
    }
}

