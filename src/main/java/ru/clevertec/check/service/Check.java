package ru.clevertec.check.service;

import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.observer.Publisher;
import ru.clevertec.check.parameters.ProductParameters;
import java.util.List;

public interface Check {

    StringBuilder showCheck(List<ProductParameters> list);

    StringBuilder htmlCheck(List<ProductParameters> list);

    void printCheck(StringBuilder sb) throws ProductException;

    void printPDFCheck(StringBuilder sb) throws ProductException;

    Publisher getPublisher();
}
