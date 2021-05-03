package ru.clevertec.check.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Sql("/users.sql")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findByIdTest() throws Exception {
        this.mockMvc.perform(get("/admin/users/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("blin4")));
    }

    @Test
    void findAllByParametersTest() throws Exception {
        this.mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    void addTest() throws Exception {
        this.mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"blin5\", \"firstName\": \"pasha\", \"password\": \"123456\", " +
                        "\"email\": \"blin@email\", \"age\": \"30\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void changeRoleTest() throws Exception {
        this.mockMvc.perform(put("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    void changeRoleNoUserTest() throws Exception {
        this.mockMvc.perform(put("/admin/users/10"))
                .andExpect(status().isNotModified());
    }

    @Test
    void deleteTest() throws Exception {
        this.mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNotExistUserTest() throws Exception {
        this.mockMvc.perform(delete("/admin/users/10"))
                .andExpect(status().isNoContent());
    }
}
