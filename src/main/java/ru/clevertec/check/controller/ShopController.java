package ru.clevertec.check.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.ControllerService;
import ru.clevertec.check.service.DataOrderService;
import ru.clevertec.check.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@EnableAutoConfiguration
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final CheckService checkService;
    private final UserService userService;
    private final DataOrderService dataOrderService;
    private final ControllerService cs;

    @GetMapping("")
    @SneakyThrows
    public StringBuilder selectProducts(HttpServletRequest request,
                                        Principal principal) {
        Integer id = userService.getUserByLogin(principal.getName()).getId();
        return checkService.getTXT(checkService.selectProducts(request), id);
    }

    @GetMapping("/orders")
    @SneakyThrows
    public ResponseEntity<?> selectProducts(Principal principal) {
        Integer id = userService.getUserByLogin(principal.getName()).getId();
        return new ResponseEntity<>(dataOrderService.getOrdersByUserId(id),
                cs.generateHttpStatusForView(dataOrderService.getOrdersByUserId(id)));
    }
}
