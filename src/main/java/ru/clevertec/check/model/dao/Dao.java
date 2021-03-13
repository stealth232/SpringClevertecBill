package ru.clevertec.check.model.dao;

import ru.clevertec.check.exception.DaoException;

public interface Dao <T>{
    boolean insert(T t) throws DaoException;
}
