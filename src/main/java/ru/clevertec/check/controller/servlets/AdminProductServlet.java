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

@WebServlet(urlPatterns = ADMIN_PRODUCT)
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stockString = req.getParameter(STOCK);
        String idString = req.getParameter(ID);
        Boolean stock = Boolean.parseBoolean(stockString);
        Integer id = Integer.valueOf(idString);
        List<ProductParameters> products = null;
        String message = null;
        RequestDispatcher request = req.getRequestDispatcher(PAGES + ADMIN + ADMIN_PRODUCT_MESSAGE);
        if (stockString.isEmpty()) {
            message = FILL_FIELDS;
        }
        if (!idString.isEmpty() && !stockString.isEmpty()) {
            try {
                if (ServiceFactory.getInstance().getProductService().updateStockById(id, stock)) {
                    message = UPDATE_STOCK + id;

                } else {
                    message = NO_PRODUCT + id;
                }
            } catch (ServiceException e) {
                message = NO_PRODUCT + id;
            }
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
