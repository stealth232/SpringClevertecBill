package ru.clevertec.check.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.check.model.user.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Sql("/test-data.sql")
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private static User user;

    @BeforeAll
    static void SetUp() {
        user = User.builder()
                .firstName("Pasha")
                .age(30).email("stealth23")
                .login("stealth23")
                .password("123456")
                .build();
    }

    @Test
    void addTest() {
        userRepository.save(user);
        User expected = userRepository.getUserByLogin("stealth23");
        assertThat(expected.getLogin()).isEqualTo(user.getLogin());
        int size = userRepository.findAll().size();
        assertEquals(5, size);
    }

    @Test
    void deleteByIdTest() {
        Integer result = userRepository.deleteUserById(10);
        assertEquals(1, result);
        int size = userRepository.findAll().size();
        assertEquals(3, size);
    }

    @Test
    void findAllTest() {
        List<User> users = userRepository.findAll();
        assertEquals(4, users.size());
        assertFalse(users.isEmpty());
    }

    @Test
    void getUserByLoginTest() {
        String login = "blin1";
        User user = userRepository.getUserByLogin(login);
        assertEquals(login, user.getLogin());
    }

    @Test
    void findUserByLoginTest() {
        String login = "blin1";
        Optional<User> user = userRepository.findUserByLogin(login);
        assertThat(user).isPresent();
    }

    @Test
    void existsByIdTest() {
        int id = 1;
        int notExistsId = 5;
        boolean user = userRepository.existsById(id);
        boolean notExistsUser = userRepository.existsById(notExistsId);
        assertTrue(user);
        assertFalse(notExistsUser);
    }

    @Test
    void getUserByLoginAndPassword() {
        String login = "blin2";
        String password = "123456";
        Optional<User> user = userRepository.getUserByLoginAndPassword(login, password);
        assertThat(user).isPresent();
    }
}