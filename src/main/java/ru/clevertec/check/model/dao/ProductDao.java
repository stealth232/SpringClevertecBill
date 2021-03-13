package ru.clevertec.check.model.dao;

import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.model.dao.connection.DBController;

import java.util.List;

public interface ProductDao extends Dao<ProductParameters> {
    
    boolean updateName(ProductParameters product) throws DaoException;

    boolean updateCost(String name, double cost) throws DaoException;

    boolean updateStockByName(String name, Boolean stock)throws DaoException;

    boolean updateStockById(Integer id, Boolean stock) throws DaoException;

    boolean deleteById(Integer id) throws DaoException;

    boolean removeProductsTable() throws DaoException;

    boolean cleanProductsTable() throws DaoException;

    boolean createTableProducts() throws DaoException;

    boolean fillProductsRepository();

    ProductParameters getId(Integer id) throws DaoException;

    int getProductsCount() throws DaoException;

    Integer getProductsSize() throws DaoException;

    List<ProductParameters> getProductList() throws DaoException;

    List<ProductParameters> getCurrentProductList() throws DaoException;
}
