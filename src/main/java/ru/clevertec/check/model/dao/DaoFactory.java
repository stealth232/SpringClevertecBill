package ru.clevertec.check.model.dao;

import ru.clevertec.check.model.dao.connection.DBController;
import ru.clevertec.check.model.dao.impl.ProductDaoImpl;
import ru.clevertec.check.model.dao.impl.UserDaoImpl;

public class DaoFactory {
    private static final DaoFactory instance = new DaoFactory();
    private final ProductDao productDao = new ProductDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    private DaoFactory(){
    }

    public static DaoFactory getInstance(){
    return instance;
    }

    public ProductDao getProductDao(){
        return productDao;
    }

    public UserDao getUserDao(){
        return userDao;
    }

}
