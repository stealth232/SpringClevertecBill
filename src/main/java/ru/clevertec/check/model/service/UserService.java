package ru.clevertec.check.model.service;

import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.exception.ServiceException;

import java.util.List;

public interface UserService {
    boolean updateUserRole(String login, String userType) throws ServiceException;

    boolean deleteByLogin(String login) throws ServiceException;

    User getUserId(Integer id) throws ServiceException;

    User getUserByLogin(String login, String password) throws ServiceException;

    boolean isLoginExist(String login) throws ServiceException;

    Integer getUsersSize() throws ServiceException;

    boolean removeUsersTable() throws ServiceException;

    boolean createTableUsers() throws ServiceException;

    boolean fillUsersRepository();

    List<User> getBaseUserList() throws ServiceException;

    List<User> getCurrentUserList() throws ServiceException;

    boolean insert(User user)throws ServiceException;
}
