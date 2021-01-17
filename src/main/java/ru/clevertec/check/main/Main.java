package ru.clevertec.check.main;

import ru.clevertec.check.creator.impl.OrderCreatorImpl;
import ru.clevertec.check.entity.*;
import ru.clevertec.check.entity.impl.CheckImpl;
import ru.clevertec.check.entity.impl.Product;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;
import ru.clevertec.check.parser.impl.ArgsParserImpl;
import java.util.*;


public class Main {

   public static void main(String[] args) throws ProductException {
        args = new String[]{"5-40", "1-70", "2-120","3-100","4-100", "3-35", "4-7"};
        //args = new String[]{"src\\main\\resources\\file.txt"};

        MyLinkedList<Product> products = new MyLinkedList<>();
        products.add(new Bounty());
        products.add(new Snickers());
        products.add(new Nuts());
        products.add(new Mars());
        products.add(new Twix());

        ArgsParserImpl ap = new ArgsParserImpl();
        OrderCreatorImpl oc = new OrderCreatorImpl();
        MyLinkedList<String> list = ap.parsParams(args);
        Map<String, Integer> map = oc.order(list);
        CheckImpl check = new CheckImpl(map);

        StringBuilder sb = check.showCheck(products);
        System.out.println(sb);
        check.printCheck(sb);
        check.printPDFCheck(sb);
   }
}
