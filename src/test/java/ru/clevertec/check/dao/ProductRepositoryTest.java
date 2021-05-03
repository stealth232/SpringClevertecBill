package ru.clevertec.check.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.model.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@Sql("/products.sql")
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private static Product product;

    @BeforeAll
    static void setUp() {
        product = Product.builder()
                .id(4)
                .name("Snickers")
                .cost(2.50)
                .stock(false)
                .build();
    }

    @Test
    void addTest() {
        System.out.println(productRepository.findAll());
        productRepository.save(product);
        Product expected = productRepository.getProductByName("Snickers");
        System.out.println(expected);
        assertThat(expected.getName()).isEqualTo(product.getName());
        int size = productRepository.findAll().size();
        assertEquals(5, size);
    }

    @Test
    void deleteByIdTest() {
        Integer result = productRepository.deleteProductById(1);
        assertEquals(1, result);
        int size = productRepository.findAll().size();
        assertEquals(4, size);
    }

    @Test
    void deleteByNameTest() {
        String name = "Twix";
        Integer result = productRepository.deleteProductByName(name);
        assertEquals(1, result);
        int size = productRepository.findAll().size();
        assertEquals(4, size);
    }

    @Test
    void findAllTest() {
        List<Product> products = productRepository.findAll();
        assertEquals(5, products.size());
        assertFalse(products.isEmpty());
    }

    @Test
    void getProductByNameTest() {
        String name = "Twix";
        Product product = productRepository.getProductByName(name);
        assertEquals(name, product.getName());
    }

    @Test
    void getProductByIdTest() {
        Integer id = 1;
        Product product = productRepository.getProductById(id);
        assertEquals(id, product.getId());
    }

    @Test
    void updateCostTest() {
        Double cost = 2.5;
        productRepository.updateCost(cost, 1);
        Product product = productRepository.getProductById(1);
        assertEquals(cost, product.getCost());
    }

    @Test
    void updateStockTest() {
        Boolean stock = Boolean.TRUE;
        productRepository.updateStock(stock, 1);
        Product product = productRepository.getProductById(1);
        assertEquals(stock, product.isStock());
    }

}
