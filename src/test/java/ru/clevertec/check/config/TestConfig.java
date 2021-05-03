package ru.clevertec.check.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.check.dao.OrderRepository;
import ru.clevertec.check.dao.WarehouseRepository;
import ru.clevertec.check.service.UserService;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {
    @Bean
    public UserService userService(){
        return mock(UserService.class);
    }

    @Bean
    public OrderRepository orderRepository(){
        return mock(OrderRepository.class);
    }

    @Bean
    public WarehouseRepository warehouseRepository(){
        return mock(WarehouseRepository.class);
    }
}
