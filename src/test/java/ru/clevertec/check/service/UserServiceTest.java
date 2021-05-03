package ru.clevertec.check.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.check.config.AppConfig;
import ru.clevertec.check.config.TestConfig;
import ru.clevertec.check.config.WebSecurityTestConfig;
import ru.clevertec.check.dao.UserRepository;
import ru.clevertec.check.model.user.User;
import ru.clevertec.check.service.impl.UserServiceImpl;

import java.util.*;


import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(UserServiceImpl.class)
@ContextConfiguration(classes = {TestConfig.class,
        AppConfig.class, WebSecurityTestConfig.class})
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    private static User user;
    private static User user2;

    @BeforeAll
    static void setUp() {
        user = User.builder()
                .id(1).firstName("Alex")
                .age(30)
                .email("stealth2322@gmail.com")
                .login("blin1")
                .password("123456")
                .build();
        user2 = User.builder()
                .id(2).firstName("Alex")
                .age(30)
                .email("stealth2322@gmail.com")
                .login("blin2")
                .password("123456")
                .build();
    }

    @AfterAll
    static void tearDown() {
        user = null;
        user2 = null;
    }

    @Test
    void findByIdTest() {
        when(userRepository.getUserById(any(Integer.class))).thenReturn(user);
        User actual = userService.findUserById(1);
        assertEquals(actual.getId(), user.getId());
    }

    @Test
    void saveTest() {
        when(userRepository.findUserByLogin(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        User actual = userService.save(user2);
        assertEquals(actual, userRepository.save(user));
        assertThat(actual, notNullValue());
    }

    @Test
    void findAllTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> actual = userService.findAll();
        assertEquals(actual, userRepository.findAll());
    }

    @Test
    public void getByIdThrowTest() {
        assertThrows(NullPointerException.class, () -> {
            userService.getUserById(5).getId();
        });
    }

    @Test
    public void getUserByLoginTest() {
        when(userRepository.findUserByLogin(any(String.class)))
                .thenReturn(Optional.of(user));
        Optional<User> actual = userService.findUserByLogin("blin1");
        System.out.println(actual.get().getLogin());
        assertEquals(actual.get().getLogin(), user.getLogin());
    }

    @Test
    public void deleteUserByIdTest() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(any(Integer.class));
        assertDoesNotThrow(() -> userService.deleteUserById(1));
    }

    @Test
    public void usersSizeTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> actual = userService.findAll();
        assertEquals(actual.size(), userRepository.findAll().size());
        System.out.println(actual.size());
    }

    @Test
    public void getUserThrowTest() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("blin1", "wrong");
        });
    }
}
