package com.avalieva.homework3.ru.otus.avalieva.testing.impl;

import com.avalieva.homework3.ru.otus.avalieva.testing.IOService;
import org.springframework.stereotype.Service;

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
