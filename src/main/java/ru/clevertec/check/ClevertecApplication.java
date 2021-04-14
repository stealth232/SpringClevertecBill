package ru.clevertec.check;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.clevertec.check.service.UserService;

@SpringBootApplication
public class ClevertecApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClevertecApplication.class, args);

    }
}
