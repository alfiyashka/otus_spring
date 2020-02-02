package ru.avalieva.otus.libraryMongoDB_hw08.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.libraryMongoDB_hw08.service.IOService;

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

