package ru.clevertec.check.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.config.TestConfig;
import ru.clevertec.check.config.WebSecurityTestConfig;
import ru.clevertec.check.dao.ProductRepository;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import(ProductServiceImpl.class)
@ContextConfiguration(classes = {TestConfig.class,
        AppConfig.class, WebSecurityTestConfig.class})
@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static Product product;
    private static Product product2;

    @BeforeAll
    static void setUp() {
        product = Product.builder()
                .id(1)
                .name("Snickers")
                .cost(2.50)
                .stock(false)
                .build();
        product2 = Product.builder()
                .id(2)
                .name("Mars")
                .cost(1.50)
                .stock(true)
                .build();
    }

    @AfterAll
    static void tearDown() {
        product = null;
        product2 = null;
    }

    @Test
    public void findAllTest() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        when(productRepository.findAll()).thenReturn(products);
        List<Product> actual = productService.findAll();
        assertEquals(actual, products);
    }

    @Test
    void findByIdTest() {
        when(productRepository.getProductById(any(Integer.class))).thenReturn(product);
        Product actual = productService.getProductById(1);
        assertEquals(actual, product);
    }

    @Test
    void deleteByIdTest() {
        when(productRepository.getProductById(any(Integer.class))).thenReturn(product);
        doNothing().when(productRepository).deleteById(any(Integer.class));
        assertDoesNotThrow(() -> productService.deleteProductById(1));
    }

    @Test
    void deleteByNameTest() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        when(productRepository.findAll()).thenReturn(products);
        when(productRepository.deleteProductByName(any(String.class))).thenReturn(1);
        List<Product> actual = productService.findAll();
        assertEquals(actual.size(), products.size());
        Integer snickers = productService.deleteProductByName("Snickers");
        assertEquals(1, snickers);
    }

    @Test
    void getProductById() {
        when(productRepository.getProductById(any(Integer.class))).thenReturn(product2);
        Product actual = productService.getProductById(1);
        Product productById = productRepository.getProductById(1);
        assertEquals(actual, productById);
    }

    @Test
    void addTest() {
        when(productRepository.getProductById(any(Integer.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product actual = productService.save(product2);
        assertThat(actual).isNotNull();
    }
}
