package ru.clevertec.check.model.service.impl;

import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.dao.DaoFactory;
import ru.clevertec.check.model.dao.ProductDao;
import ru.clevertec.check.model.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = DaoFactory.getInstance().getProductDao();

    @Override
    public boolean updateName(ProductParameters product) throws ServiceException {
        try {
            return productDao.updateName(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean insert(ProductParameters product) throws ServiceException {
        try {
            return productDao.insert(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateCost(String name, double cost) throws ServiceException {
        try {
            return productDao.updateCost(name, cost);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateStockByName(String name, Boolean stock) throws ServiceException {
        try {
            return productDao.updateStockByName(name, stock);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateStockById(Integer id, Boolean stock) throws ServiceException {
        try {
            return productDao.updateStockById(id, stock);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) throws ServiceException {
        try {
            return productDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean removeProductsTable() throws ServiceException {
        try {
            return productDao.removeProductsTable();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean cleanProductsTable() throws ServiceException {
        try {
            return productDao.cleanProductsTable();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean createTableProducts() throws ServiceException {
        try {
            return productDao.createTableProducts();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean fillProductsRepository() {
        return productDao.fillProductsRepository();
    }

    @Override
    public ProductParameters getId(Integer id) throws ServiceException {
        try {
            return productDao.getId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getProductsCount() throws ServiceException {
        try {
            return productDao.getProductsCount();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer getProductsSize() throws ServiceException {
        try {
            return productDao.getProductsSize();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ProductParameters> getProductList() throws ServiceException {
        try {
            return productDao.getProductList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ProductParameters> getCurrentProductList() throws ServiceException {
        try {
            return productDao.getCurrentProductList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
