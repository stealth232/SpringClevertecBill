package ru.clevertec.check.controller.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.entities.product.Order;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.CheckService;
import ru.clevertec.check.model.service.OrderCreatorService;
import ru.clevertec.check.model.service.ServiceFactory;
import ru.clevertec.check.model.service.ArgsParserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.model.service.CheckConstants.*;
import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = SHOP)
public class MainServlet extends HttpServlet {
    static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType(TEXT_HTML);
        String[] itemId = req.getParameterValues(ITEM_ID);
        String[] quantity = req.getParameterValues(QUANTITY);
        String card = req.getParameter(CARD);
        String[] resultCheck = new String[itemId.length + 1];
        for (int i = 0; i < itemId.length; i++) {
            if (itemId[i].isEmpty() || quantity[i].isEmpty()) {
                itemId[i] = ZERO;
                quantity[i] = ZERO;
            }
            resultCheck[i] = itemId[i] + URL_DELIMETER + quantity[i];
            if (!card.isEmpty()) {
                resultCheck[itemId.length] = URL_CARD + card;
            } else if (card.isEmpty()) {
                resultCheck[itemId.length] = URL_CARD + ZERO;
            }
        }
        try {
            List<ProductParameters> products = ServiceFactory.getInstance().getProductService().getProductList();
            ArgsParserService argsParserService = ServiceFactory.getInstance().getArgsParser();
            OrderCreatorService orderCreator = ServiceFactory.getInstance().getOrderCreator();
            List<String> list = argsParserService.parsParams(resultCheck);
            Map<String, Integer> map = orderCreator.makeOrder(list);
            CheckService checkService = ServiceFactory.getInstance().getCheckService(map);
            StringBuilder html = checkService.getHTML(products);
            Order order = checkService.getOrder(products);
            checkService.printPDFCheck(checkService.getPDF(products));
            req.setAttribute("html", html);
            req.setAttribute("list", order.getProducts());
            req.setAttribute("order", order);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(SHOP + CHECK);
            requestDispatcher.forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
