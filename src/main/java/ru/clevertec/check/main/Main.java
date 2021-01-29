package ru.clevertec.check.main;

import ru.clevertec.check.observer.entity.State;
import ru.clevertec.check.observer.listeners.Consoler;
import ru.clevertec.check.observer.listeners.Emailer;
import ru.clevertec.check.observer.listeners.EventListener;
import ru.clevertec.check.service.Check;
import ru.clevertec.check.utils.creator.OrderCreator;
import ru.clevertec.check.utils.creator.impl.OrderCreatorImpl;
import ru.clevertec.check.dao.DBController;
import ru.clevertec.check.dao.Repository;
import ru.clevertec.check.service.impl.CheckImpl;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.utils.mylinkedlist.impl.MyLinkedList;
import ru.clevertec.check.parameters.ProductParameters;
import ru.clevertec.check.utils.parser.ArgParser;
import ru.clevertec.check.utils.parser.impl.ArgsParserImpl;
import ru.clevertec.check.utils.proxy.ProxyFactory;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ProductException {
        args = new String[]{"1-40", "2-70", "3-120", "4-100", "5-100", "6-35", "7-7", "6-35", "card-5678"};
        //args = new String[]{"src\\main\\resources\\file.txt"};

        DBController database = new DBController();
        Repository repository = Repository.getInstance(database);
        repository.removeTable();
        repository.createTable();
        repository.fillRepository();

        List<ProductParameters> products = new MyLinkedList<>();
        List<ProductParameters> productsProxy = (List<ProductParameters>) ProxyFactory.doProxy(products);
        for (int i = 1; i < repository.getSize() + 1; i++) {
            productsProxy.add(repository.getId(i));
        }
        ArgParser argParser = new ArgsParserImpl();
        OrderCreator orderCreator = new OrderCreatorImpl();
        List<String> list = argParser.parsParams(args);
        Map<String, Integer> map = orderCreator.makeOrder(list);
        Check check = new CheckImpl(map);
        StringBuilder sb = check.showCheck(products);
        System.out.println(sb);
        EventListener consoler = new Consoler();
        EventListener emailer = new Emailer();
        check.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_TXT, consoler);
        check.getPublisher().subscribe(State.CHECK_WAS_PRINTED_IN_PDF, emailer);
        check.printCheck(sb);
        check.printPDFCheck(sb);
    }
}
