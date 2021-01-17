package ru.clevertec.check.parser;

import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;

public interface ArgParser {
    MyLinkedList<String> parsParams(String[] args) throws ProductException;

}
