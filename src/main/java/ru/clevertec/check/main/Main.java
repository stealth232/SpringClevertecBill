package ru.clevertec.check.main;

import ru.clevertec.check.observer.entity.State;
import ru.clevertec.check.observer.listeners.Consoler;
import ru.clevertec.check.observer.listeners.Emailer;
import ru.clevertec.check.observer.listeners.EventListener;
import ru.clevertec.check.service.Check;
import ru.clevertec.check.utils.creator.OrderCreator;
import ru.clevertec.check.utils.creator.impl.OrderCreatorImpl;
import ru.clevertec.check.dao.Repository;
import ru.clevertec.check.service.impl.CheckImpl;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.utils.mylinkedlist.MyLinkedList;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.utils.parser.ArgParser;
import ru.clevertec.check.utils.parser.impl.ArgsParserImpl;
import ru.clevertec.check.utils.proxy.ProxyFactory;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ProductException {
        args = new String[]{"1-40", "2-70", "3-120", "4-100", "5-100", "6-35", "7-7", "6-35", "card-5678"};
        //args = new String[]{"src\\main\\resources\\file.txt"};

        Repository repository = Repository.getInstance();
        repository.removeTable();
        repository.createTable();
        repository.fillRepository();

        List<ProductParameters> products = new MyLinkedList<>();
        List<ProductParameters> productsProxy = (List<ProductParameters>) ProxyFactory.doProxy(products);
        for (int i = 1; i < repository.getSize() + 1; i++) {
            productsProxy.add(repository.getId(i));
        }

        ArgsParserImpl argParser = new ArgsParserImpl();
        OrderCreatorImpl orderCreator = new OrderCreatorImpl();
        ArgParser argParserProxy = (ArgParser) ProxyFactory.doProxy(argParser);
        OrderCreator orderCreatorProxy = (OrderCreator) ProxyFactory.doProxy(orderCreator);
        List<String> list = argParserProxy.parsParams(args);
        Map<String, Integer> map = orderCreatorProxy.makeOrder(list);
        CheckImpl check = new CheckImpl(map);
        Check checkProxy = (Check) ProxyFactory.doProxy(check);
        StringBuilder sb = checkProxy.showCheck(products);
        System.out.println(sb);

        EventListener consoler = new Consoler();
        EventListener emailer = new Emailer();
        check.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_TXT, consoler);
        check.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_PDF, emailer);
        checkProxy.printCheck(sb);
        checkProxy.printPDFCheck(sb);
    }
}
