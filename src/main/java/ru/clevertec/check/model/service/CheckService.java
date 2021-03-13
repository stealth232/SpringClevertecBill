package ru.clevertec.check.model.service;

import ru.clevertec.check.entities.product.Order;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.observer.Publisher;

import java.util.List;

public interface CheckService {

    StringBuilder showCheck(List<ProductParameters> list) throws ServiceException;

    StringBuilder getHTML(List<ProductParameters> list) throws ServiceException;

    void printCheck(StringBuilder sb) throws ServiceException;

    void printPDFCheck(StringBuilder sb) throws ServiceException;

    Publisher getPublisher();

    StringBuilder getPDF(List<ProductParameters> products) throws ServiceException;

    Order getOrder(List<ProductParameters> list) throws ServiceException;

}
