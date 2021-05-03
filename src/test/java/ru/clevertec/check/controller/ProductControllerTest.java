package ru.clevertec.check.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.config.TestConfig;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Sql("/products.sql")
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, AppConfig.class})
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getByIdTest() throws Exception {
        this.mockMvc.perform(get("admin/products/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alonka")));
    }

    @Test
    void findAllTest() throws Exception {
        this.mockMvc.perform(get("admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)));
    }

    @Test
    void addTest() throws Exception {
        this.mockMvc.perform(post("admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"GreenField\", \"cost\": 2, \"stock\": \"true\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", notNullValue()));
    }

    @Test
    void updateStockTest() throws Exception {
        this.mockMvc.perform(put("admin/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(true)));
    }

    @Test
    void deleteTest() throws Exception {
        this.mockMvc.perform(delete("admin/products/6"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCostTest() throws Exception {
        this.mockMvc.perform(patch("admin/products/1?cost=4.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost", is(4.5)));
    }
}
