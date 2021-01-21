package ru.clevertec.check.entity;

import ru.clevertec.check.entity.impl.Product;
import ru.clevertec.check.exception.ProductException;

import java.util.List;

public interface Check {

    StringBuilder showCheck(List<Product> list);
    StringBuilder htmlCheck(List<Product> list);
    void printCheck(StringBuilder sb)throws ProductException;
    void printPDFCheck(StringBuilder sb) throws ProductException;
}
