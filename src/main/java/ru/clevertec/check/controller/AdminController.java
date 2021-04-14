package ru.clevertec.check.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.check.model.product.Order;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.model.user.User;
import ru.clevertec.check.service.ControllerService;
import ru.clevertec.check.service.DataOrderService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.service.UserService;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final ProductService productService;
    private final ControllerService cs;
    private final DataOrderService dos;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, cs.generateHttpStatusForView(users));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return new ResponseEntity<>(cs.generateHttpStatusForDeletion(userService.deleteUserById(id)));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, cs.generateHttpStatusForView(user));
    }

    @GetMapping("/users/orders/{id}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Integer id) {
        List<Order> orders = dos.getOrdersByUserId(id);
        return new ResponseEntity<>(orders, cs.generateHttpStatusForView(orders));
    }

    @DeleteMapping("/users/orders/{id}")
    public ResponseEntity<?> deleteOrdersByUserId(@PathVariable Integer id) {
        Integer deleted = dos.removeDataOrdersByUserId(id);
        return new ResponseEntity<>(deleted,
                cs.generateHttpStatusForDeletion(deleted));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> changeRole(@PathVariable Integer id) {
        return new ResponseEntity<>(cs.generateHttpStatus(userService.changeRole(id)));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, cs.generateHttpStatusForView(products));
    }

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(savedProduct, cs.generateHttpStatusForSave(savedProduct));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> changeStock(@PathVariable Integer id) {
        return new ResponseEntity<>(cs.generateHttpStatus(productService.changeStockById(id)));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(cs.generateHttpStatusForDeletion(productService.deleteProductById(id)));
    }

    @PatchMapping("/products/cost/{id}")
    public ResponseEntity<?> setCost(@RequestParam("cost") double cost,
                                     @PathVariable Integer id) {
        return new ResponseEntity<>(cs.generateHttpStatus(productService.updateCost(cost, id)));
    }
}
