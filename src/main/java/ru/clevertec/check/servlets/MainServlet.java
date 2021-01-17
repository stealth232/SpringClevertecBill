package ru.clevertec.check.servlets;

import ru.clevertec.check.creator.impl.OrderCreatorImpl;
import ru.clevertec.check.entity.*;
import ru.clevertec.check.entity.impl.CheckImpl;
import ru.clevertec.check.entity.impl.Product;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;
import ru.clevertec.check.parser.impl.ArgsParserImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/hello")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        String[] itemId = req.getParameterValues("itemId");
        String[] quantity = req.getParameterValues("quantity");
        String card = req.getParameter("card");
        String[]  resultCheck = new String[itemId.length+1];

        for (int i=0; i<itemId.length; i++){
            resultCheck[i]= itemId[i]+"-"+quantity[i];
            if(!card.isBlank()){
                resultCheck[itemId.length]= "card-"+card;
            }
            else if(card.isBlank()){
                resultCheck[itemId.length] = null;
            }
        }

        MyLinkedList<Product> products = new MyLinkedList<>(); //список продуктов
        products.add(new Bounty());
        products.add(new Snickers());
        products.add(new Nuts());
        products.add(new Mars());
        products.add(new Twix());

        ArgsParserImpl ap = new ArgsParserImpl();
        OrderCreatorImpl oc = new OrderCreatorImpl();
        MyLinkedList<String> list = null;

        try {
            list = ap.parsParams(resultCheck);
        } catch (ProductException e) {
            e.printStackTrace();
        }
        Map<String, Integer> map = null;
        try {
            map = oc.order(list);
        } catch (ProductException e) {
            e.printStackTrace();
        }
        CheckImpl check = new CheckImpl(map);
        StringBuilder sb = check.showCheck(products);
        StringBuilder html = check.htmlCheck(products);
        System.out.println(sb);

        try {
            writer.print(html);
        } finally {
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        super.doPost(req, resp);
    }
}
