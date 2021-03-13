package ru.clevertec.check.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.model.dao.connection.DBController;
import ru.clevertec.check.model.dao.ProductDao;
import ru.clevertec.check.utils.builders.Builder;
import ru.clevertec.check.utils.builders.ProductBuilder;
import ru.clevertec.check.utils.mylinkedlist.MyLinkedList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.check.exception.ProductExceptionConstants.SQL_EXCEPTION;

public class ProductDaoImpl implements ProductDao {
    static Logger logger = LogManager.getLogger();
    private static DBController controller = new DBController();
    private static ProductDaoImpl instance = new ProductDaoImpl();

    private static final String insertProductQuery = "insert into products (name, cost, stock) values (?, ?, ?)";
    private static final String updateQuery = "update products set name = ? where id = ?";
    private static final String updateProductsCostQuery = "update products set cost = ? where name = ?";
    private static final String updateStockByNameQuery = "update products set stock = ? where name = ?";
    private static final String updateStockByIdQuery = "update products set stock = ? where id = ?";
    private static final String truncateQuery = "truncate table products";
    private static final String dropQuery = "drop table products";
    private static final String deleteQuery = "delete from products where id = ?";
    private static final String selectProductQuery = "select * from products where id = ?";
    private static final String findMaxProductId = "select MAX(id) from products";
    private static final String getSizeQuery = "SELECT COUNT (*) AS rowcount FROM products";
    private static final String createQuery = "create table if not exists products " +
            "(" +
            "id serial constraint product_pk primary key, " +
            "name varchar(255) UNIQUE , " +
            "cost double precision, " +
            "stock boolean);";

    public static ProductDaoImpl getInstance() {
        return instance;
    }

    public ProductDaoImpl() {
    }

    @Override
    public boolean insert(ProductParameters product) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertProductQuery)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getCost());
            statement.setBoolean(3, product.isStock());
            int rows = statement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean updateName(ProductParameters product) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getItemId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean updateCost(String name, double cost) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateProductsCostQuery)) {
            statement.setDouble(1, cost);
            statement.setString(2, name);
            return statement.executeUpdate() == 1;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean updateStockByName(String name, Boolean stock) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateStockByNameQuery)) {
            statement.setBoolean(1, stock);
            statement.setString(2, name);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean updateStockById(Integer id, Boolean stock) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateStockByIdQuery)) {
            statement.setBoolean(1, stock);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean deleteById(Integer id) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public ProductParameters getId(Integer id) throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectProductQuery)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Builder productBuilder = new ProductBuilder();
            if (resultSet.next()) {
                productBuilder.setId(resultSet.getInt(1));
                productBuilder.setCost(resultSet.getDouble(3));
                productBuilder.setName(resultSet.getString(2));
                productBuilder.setStock(resultSet.getBoolean(4));
            }
            return productBuilder.getProduct();
        } catch (SQLException e) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public int getProductsCount() throws DaoException {
        int count = 0;
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(findMaxProductId)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public Integer getProductsSize() throws DaoException {
        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getSizeQuery);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean removeProductsTable() throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(dropQuery)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean cleanProductsTable() throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(truncateQuery)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean createTableProducts() throws DaoException {
        try (Connection connection = controller.getConnection();
             PreparedStatement statement = connection.prepareStatement(createQuery)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throw new DaoException(SQL_EXCEPTION);
        }
    }

    @Override
    public boolean fillProductsRepository() {
        try {
            Builder snickers = new ProductBuilder();
            snickers.setName("Snickers");
            snickers.setCost(1.3);
            snickers.setStock(false);
            instance.insert(snickers.getProduct());

            Builder twix = new ProductBuilder();
            twix.setName("Twix");
            twix.setCost(1.56);
            twix.setStock(true);
            instance.insert(twix.getProduct());

            Builder mars = new ProductBuilder();
            mars.setName("Mars");
            mars.setCost(1.67);
            mars.setStock(false);
            instance.insert(mars.getProduct());

            Builder kitkat = new ProductBuilder();
            kitkat.setName("KitKat");
            kitkat.setCost(1.2);
            kitkat.setStock(true);
            instance.insert(kitkat.getProduct());

            Builder alonka = new ProductBuilder();
            alonka.setName("Alonka");
            alonka.setCost(2.3);
            alonka.setStock(false);
            instance.insert(alonka.getProduct());

            Builder nuts = new ProductBuilder();
            nuts.setName("Nuts");
            nuts.setCost(2.1);
            nuts.setStock(true);
            instance.insert(nuts.getProduct());

            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public List<ProductParameters> getProductList() throws DaoException {
        List<ProductParameters> list = new MyLinkedList<>();
        instance.createTableProducts();
        instance.fillProductsRepository();
        for (int i = 1; i < getProductsCount() + 1; i++) {
            try {
                list.add(instance.getId(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<ProductParameters> getCurrentProductList() throws DaoException {
        List<ProductParameters> list = new ArrayList<>();
        for (int i = 0; i <= getProductsCount(); i++) {
            try {
                if (instance.getId(i).getName() != null) {
                    list.add(instance.getId(i));
                }
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
