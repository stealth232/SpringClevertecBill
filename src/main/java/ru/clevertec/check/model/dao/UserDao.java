package ru.clevertec.check.model.dao;

import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.exception.DaoException;

import java.util.List;

public interface UserDao extends Dao<User>{

    boolean updateUserRole(String login, String userType) throws DaoException;

    boolean deleteByLogin(String login)throws DaoException;

    User getUserId(Integer id) throws DaoException;

    User getUserByLogin(String login, String password) throws DaoException;

    boolean isLoginExist(String login) throws DaoException;

    Integer getUsersSize() throws DaoException;

    boolean removeUsersTable() throws DaoException;

    boolean createTableUsers()throws DaoException;

    boolean fillUsersRepository();

    List<User> getBaseUserList() throws DaoException;

    List<User> getCurrentUserList() throws DaoException;

}
