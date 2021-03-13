package ru.clevertec.check.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.entities.user.UserType;
import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.model.dao.connection.DBController;
import ru.clevertec.check.model.dao.UserDao;
import ru.clevertec.check.utils.mylinkedlist.MyLinkedList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.exception.ProductExceptionConstants.SQL_EXCEPTION;

public class UserDaoImpl implements UserDao {
    static Logger logger = LogManager.getLogger();
    private static DBController controller = new DBController();
    private static UserDaoImpl instance = new UserDaoImpl();

    private static final String insertUserQuery = "insert into users (first_name, second_name, age, usertype," +
            "login, password) values (?, ?, ?, ?, ?, ?)";
    private static final String updateUserQuery = "update users set usertype = ? where login = ?";
    private static final String dropUsersQuery = "drop table users";
    private static final String deleteUserByLogin = "delete from users where login = ?";
    private static final String selectUserQuery = "select * from users where id = ?";
    private static final String findMaxUserstId = "select MAX(id) from users";
    private static final String selectUserByLoginQuery = "select * from users where login = ? and password = ?";
    private static final String findLoginQuery = "select * from users where login = ?";
    private static final String getUsersSizeQuery = "SELECT COUNT (*) AS rowcount FROM users";
    private static final String createUsersQuery = "create table if not exists users " +
            "(" +
            "id serial constraint user_pk primary key," +
            "first_name varchar(255)," +
            "second_name varchar(255)," +
            "age int," +
            "usertype varchar(255)," +
            "login varchar(255) unique," +
            "password varchar(255));";

    public static UserDaoImpl getInstance() {
        return instance;
    }

    public UserDaoImpl() {
    }
    @Override
    public boolean insert(User user) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertUserQuery)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSecondName());
            statement.setInt(3, user.getAge());
            statement.setString(4, String.valueOf(user.getUserType()));
            statement.setString(5, user.getCredentials().entrySet().stream().findFirst().get().getKey());
            statement.setString(6, user.getCredentials().entrySet().stream().findFirst().get().getValue());
            int rows = statement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean updateUserRole(String login, String userType) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateUserQuery)) {
            statement.setString(1, userType.toUpperCase());
            statement.setString(2, login);
            return statement.executeUpdate() == 1;
        } catch (Exception e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean deleteByLogin(String login) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteUserByLogin)) {
            statement.setString(1, login);
            return statement.executeUpdate() == 1;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public User getUserId(Integer id) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectUserQuery)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            Map<String, String> credentials = new HashMap<>();
            if (resultSet.next()) {
                credentials.put(resultSet.getString(6), resultSet.getString(7));
                user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), credentials, UserType.valueOf(resultSet.getString(5)));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public User getUserByLogin(String login, String password) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectUserByLoginQuery)) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            Map<String, String> credentials = new HashMap<>();
            if (resultSet.next()) {
                credentials.put(resultSet.getString(6), resultSet.getString(7));
                user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), credentials, UserType.valueOf(resultSet.getString(5)));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }
    @Override
    public boolean isLoginExist(String login) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(findLoginQuery)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public Integer getUsersSize() throws DaoException {
        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getUsersSizeQuery);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean removeUsersTable() throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(dropUsersQuery)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean createTableUsers() throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(createUsersQuery)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean fillUsersRepository() {
        try {
            Map<String, String> credentials1 = new HashMap<>();
            credentials1.put("beckham", "123456");
            instance.insert(new User("David", "Beckham", 43, credentials1, UserType.USER));

            Map<String, String> credentials = new HashMap<>();
            credentials.put("stealth23", "123456");
            instance.insert(new User("Pasha", "Blintsov", 30, credentials, UserType.ADMIN));

            Map<String, String> credentials2 = new HashMap<>();
            credentials2.put("zlatan", "123456");
            instance.insert(new User("Zlatan", "Ibrahimovich", 37, credentials2, UserType.USER));

            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public List<User> getBaseUserList() throws DaoException {
        List<User> list = new MyLinkedList<>();
        instance.removeUsersTable();
        instance.createTableUsers();
        instance.fillUsersRepository();
        for (int i = 0; i < instance.getUsersSize(); i++) {
            try {
                list.add(instance.getUserId(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<User> getCurrentUserList() throws DaoException {
        List<User> list = new ArrayList<>();

        for (int i = 0; i <= instance.getUsersCount(); i++) {
            try {
                if (instance.getUserId(i) != null)
                    list.add(instance.getUserId(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getUsersCount() throws DaoException {
        int count = 0;
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(findMaxUserstId)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }
}
