package ru.clevertec.check.main;

import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ArgsParserService;
import ru.clevertec.check.model.service.CheckService;
import ru.clevertec.check.model.service.OrderCreatorService;
import ru.clevertec.check.model.service.ServiceFactory;
import ru.clevertec.check.observer.entity.State;
import ru.clevertec.check.observer.listeners.Consoler;
import ru.clevertec.check.observer.listeners.Emailer;
import ru.clevertec.check.utils.proxy.ProxyFactory;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ServiceException {
        args = new String[]{"1-10", "2-20", "3-100", "4-100", "5-100", "6-100", "88-5", "card-345"};

        List<ProductParameters> products = ServiceFactory.getInstance().getProductService().getProductList();
        ArgsParserService argsParserService = ServiceFactory.getInstance().getArgsParser();
        ArgsParserService argsParser = (ArgsParserService) ProxyFactory.doProxy(argsParserService);
        OrderCreatorService orderCreatorService = ServiceFactory.getInstance().getOrderCreator();
        OrderCreatorService orderCreator = (OrderCreatorService) ProxyFactory.doProxy(orderCreatorService);
        List<String> list = argsParser.parsParams(args);
        Map<String, Integer> orderMap = orderCreator.makeOrder(list);
        CheckService checkService = ServiceFactory.getInstance().getCheckService(orderMap);
        CheckService checkProxy = (CheckService) ProxyFactory.doProxy(checkService);
        System.out.println(checkService.showCheck(products));
        checkService.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_TXT, new Consoler());
        checkService.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_PDF, new Emailer());
        checkProxy.printCheck(checkProxy.showCheck(products));
        checkProxy.printPDFCheck(checkProxy.getPDF(products));
    }
}
