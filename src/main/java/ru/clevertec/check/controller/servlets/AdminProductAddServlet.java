package ru.clevertec.check.controller.servlets;

import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.entities.product.Product;
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

@WebServlet(urlPatterns = ADMIN_ADD_PRODUCT)
public class AdminProductAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter(NAME);
        String stockString = req.getParameter(STOCK);
        String costString = req.getParameter(COST);
        List<ProductParameters> products = null;
        String message = null;
        ProductParameters product;
        RequestDispatcher request = req.getRequestDispatcher(PAGES + ADMIN + ADMIN_PRODUCT_MESSAGE);
        if (name != null && stockString != null && costString != null) {
            double cost = Double.parseDouble(costString);
            boolean stock = Boolean.parseBoolean(stockString);
            product = new Product(name, cost, stock);
            try {
                if (ServiceFactory.getInstance().getProductService().insert(product)) {
                    message = SUCCESS_ADD + name;
                }
            } catch (ServiceException e) {
                message = PRODUCT_IS_EXIST;
            }
        }
        try {
            products = ServiceFactory.getInstance().getProductService().getCurrentProductList();

        } catch (ServiceException e) {
            message = PRODUCT_IS_EXIST;
        }
        req.setAttribute(MESSAGE, message);
        req.setAttribute(PRODUCTS, products);
        request.forward(req, resp);
    }
}