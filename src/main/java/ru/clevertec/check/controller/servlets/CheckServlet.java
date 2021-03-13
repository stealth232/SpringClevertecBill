package ru.clevertec.check.controller.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.clevertec.check.model.service.CheckConstants.*;
import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = SHOP + CHECK)
public class CheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(PAGES + CHECK_JSP);
        resp.setContentType(TEXT_HTML);
        requestDispatcher.forward(req,resp);
    }
}
