package ru.clevertec.check.model.service;

import ru.clevertec.check.model.service.impl.*;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();
    private Map<String, Integer> map = new HashMap<>();
    private final ProductService productService = new ProductServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final OrderCreatorService orderCreator = new OrderCreatorServiceImpl();
    private final ArgsParserService argsParser = new ArgsParserServiceImpl();
    private final MailService mailService = new MailServiceImpl();
    private final CheckService checkService = new CheckServiceImpl(map);

    private ServiceFactory() {
    }

    public OrderCreatorService getOrderCreator() {
        return orderCreator;
    }

    public ArgsParserService getArgsParser() {
        return argsParser;
    }

    public CheckService getCheckService(Map<String, Integer> orderMap) {
        return new CheckServiceImpl(orderMap);
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public ProductService getProductService() {
        return productService;
    }

    public UserService getUserService() {
        return userService;
    }

    public MailService getMailService() {
        return mailService;
    }
}
