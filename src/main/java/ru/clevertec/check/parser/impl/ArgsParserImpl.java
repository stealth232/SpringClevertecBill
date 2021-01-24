package ru.clevertec.check.parser.impl;

import ru.clevertec.check.annotations.LogLevel;
import ru.clevertec.check.annotations.LogMe;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;
import ru.clevertec.check.parser.ArgParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgsParserImpl implements ArgParser {

    private static final String DELIMITER = "\\s";
    @LogMe(LogLevel.FATAL)
    @Override
    public MyLinkedList<String> parsParams(String[] args) throws ProductException {
        List<String> products = null;
        try {
            args[0].isEmpty();
        }catch(IndexOutOfBoundsException e){
            throw new ProductException("Try buy something");
        }
        if (args[0].contains("txt") || args[0].contains("xml")|| args[0].contains("bin")) {
            String filePath = args[0];
            try (FileReader fileReader = new FileReader(filePath); 
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                 products = Arrays.asList(bufferedReader.readLine().split(DELIMITER));
            } catch (FileNotFoundException e) {
                throw new ProductException("File not found: " + filePath, e);
            } catch (IOException e) {
                throw new ProductException("File reading error: " + filePath, e);
            }
        } else {
            products = Arrays.stream(args).collect(Collectors.toList());
        }
        MyLinkedList<String> myLinkedList = new MyLinkedList<>();
        for (int i=0; i<= products.size()-1; i++){
            myLinkedList.add(products.get(i));
        }

        return myLinkedList;
    }

    public List<String> parsParams(String filePath) throws ProductException {

        List<String> products;
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            products = Arrays.asList(bufferedReader.readLine().split(DELIMITER));
        } catch (FileNotFoundException e) {
            throw new ProductException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new ProductException("File reading error: " + filePath, e);
        }
        return products;
    }

}
