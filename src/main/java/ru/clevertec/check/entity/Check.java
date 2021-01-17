package ru.clevertec.check.entity;

import ru.clevertec.check.entity.impl.Product;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;

public interface Check {

    StringBuilder showCheck(MyLinkedList<Product> list);
    StringBuilder htmlCheck(MyLinkedList<Product> list);
    void printCheck(StringBuilder sb)throws ProductException;
    void printPDFCheck(StringBuilder sb) throws ProductException;
}
