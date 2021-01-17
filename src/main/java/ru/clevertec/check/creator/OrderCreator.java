package ru.clevertec.check.creator;

import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;

import java.util.Map;

public interface OrderCreator {
    Map<String, Integer> order(MyLinkedList<String> list) throws ProductException;
}
