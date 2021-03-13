package ru.clevertec.check.controller.servlets;

import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = ADMIN_COST)
public class AdminProductCostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(NAME);
        String costString = req.getParameter(COST);
        Double cost = Double.valueOf(costString);
        String message;
        List<ProductParameters> products = null;
        RequestDispatcher request = req.getRequestDispatcher(PAGES + ADMIN + ADMIN_PRODUCT_MESSAGE);
        if (cost <= 0) {
            message = ENTER_COST;
        } else if (!name.isEmpty() && !costString.isEmpty()) {
            try {
                if (ServiceFactory.getInstance().getProductService().updateCost(name, cost)) {
                    message = UPDATE_COST + name;
                } else {
                    message = NO_PRODUCT + name;
                }
            } catch (ServiceException e) {
                message = NO_PRODUCT + name;
            }
        } else {
            message = NO_PRODUCT + name;
        }
        try {
            products = ServiceFactory.getInstance().getProductService().getCurrentProductList();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        req.setAttribute(MESSAGE, message);
        req.setAttribute(PRODUCTS, products);
        request.forward(req, resp);
    }
}
