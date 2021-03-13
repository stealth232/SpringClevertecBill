package ru.clevertec.check.model.service;

import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.ServiceException;

import java.util.List;

public interface ProductService {

    boolean updateName(ProductParameters product) throws ServiceException;

    boolean insert(ProductParameters product) throws ServiceException;

    boolean updateCost(String name, double cost) throws ServiceException;

    boolean updateStockByName(String name, Boolean stock)throws ServiceException;

    boolean updateStockById(Integer id, Boolean stock) throws ServiceException;

    boolean deleteById(Integer id) throws ServiceException;

    boolean removeProductsTable() throws ServiceException;

    boolean cleanProductsTable() throws ServiceException;

    boolean createTableProducts() throws ServiceException;

    boolean fillProductsRepository();

    ProductParameters getId(Integer id) throws ServiceException;

    int getProductsCount() throws ServiceException;

    Integer getProductsSize() throws ServiceException;

    List<ProductParameters> getProductList() throws ServiceException;

    List<ProductParameters> getCurrentProductList() throws ServiceException;
}
