package ru.clevertec.check.model.service.impl;

import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.dao.DaoFactory;
import ru.clevertec.check.model.dao.UserDao;
import ru.clevertec.check.model.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = DaoFactory.getInstance().getUserDao();

    @Override
    public boolean updateUserRole(String login, String userType) throws ServiceException {
        try {
            return userDao.updateUserRole(login, userType);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteByLogin(String login) throws ServiceException {
        try {
            return userDao.deleteByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUserId(Integer id) throws ServiceException {
        try {
            return userDao.getUserId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUserByLogin(String login, String password) throws ServiceException {
        try {
            return userDao.getUserByLogin(login, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isLoginExist(String login) throws ServiceException {
        try {
            return userDao.isLoginExist(login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer getUsersSize() throws ServiceException {
        try {
            return userDao.getUsersSize();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean removeUsersTable() throws ServiceException {
        try {
            return userDao.removeUsersTable();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean createTableUsers() throws ServiceException {
        try {
            return userDao.createTableUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean fillUsersRepository() {
        return userDao.fillUsersRepository();
    }

    @Override
    public List<User> getBaseUserList() throws ServiceException {
        try {
            return userDao.getBaseUserList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getCurrentUserList() throws ServiceException {
        try {
            return userDao.getCurrentUserList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean insert(User user) throws ServiceException {
        try {
            return userDao.insert(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
