package ru.clevertec.check.main;

import ru.clevertec.check.creator.OrderCreator;
import ru.clevertec.check.creator.impl.OrderCreatorImpl;
import ru.clevertec.check.dao.DBController;
import ru.clevertec.check.dao.Repository;
import ru.clevertec.check.entity.*;
import ru.clevertec.check.entity.impl.CheckImpl;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;
import ru.clevertec.check.parameters.ProductParameters;
import ru.clevertec.check.parser.ArgParser;
import ru.clevertec.check.parser.impl.ArgsParserImpl;
import ru.clevertec.check.proxy.ProxyFactory;
import java.util.List;
import java.util.Map;

public class Main {

     public static void main(String[] args) throws ProductException {
         args = new String[]{"5-40", "1-70", "2-120", "3-100", "4-100", "3-35", "4-7"};
//         args = new String[]{"src\\main\\resources\\file.txt"};


         DBController database = new DBController();
         Repository repository = Repository.getInstance(database);
         repository.removeTable(); // удаление таблицы
         repository.createTable(); // создание таблицы
         repository.fillRepository(); // наполнение товарами из созданных объектов

         List<ProductParameters> products = new MyLinkedList<>();
         List<ProductParameters> productsProxy = (List<ProductParameters>) ProxyFactory.doProxy(products);
         for (int i = 1; i < repository.getSize() + 1; i++){ // перевод товаров из таблицы в объекты
             productsProxy.add(repository.getId(i));
         }

         ArgsParserImpl argParser = new ArgsParserImpl();
         OrderCreatorImpl orderCreator = new OrderCreatorImpl();
         ArgParser argParserProxy = (ArgParser) ProxyFactory.doProxy(argParser);
         OrderCreator orderCreatorProxy = (OrderCreator) ProxyFactory.doProxy(orderCreator);
         MyLinkedList<String> list = argParserProxy.parsParams(args);
         Map<String, Integer> map = orderCreatorProxy.order(list);
         CheckImpl check = new CheckImpl(map);
         Check checkProxy = (Check) ProxyFactory.doProxy(check);
         StringBuilder sb = checkProxy.showCheck(products);
         System.out.println(sb);
         checkProxy.printCheck(sb);
         checkProxy.printPDFCheck(sb);


     }
}
