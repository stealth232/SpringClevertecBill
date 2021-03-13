package ru.clevertec.check.controller.servlets;

import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.check.model.service.CheckConstants.TEXT_HTML;
import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = CREATE)
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(TEXT_HTML);
        try {
            String firstName = req.getParameter(FIRST_NAME);
            String secondName = req.getParameter(SECOND_NAME);
            int age = Integer.valueOf(req.getParameter(AGE));
            String login = req.getParameter(LOGIN);
            String password = req.getParameter(PASSWORD);
            if (ServiceFactory.getInstance().getUserService().isLoginExist(login)) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(PAGES + ERRORS + ERROR_LOGIN);
                requestDispatcher.forward(req, resp);
            } else {
                Map<String, String> credentials = new HashMap<>();
                credentials.put(login, password);
                User user = new User(firstName, secondName, age, credentials);
                ServiceFactory.getInstance().getUserService().insert(user);
                req.setAttribute(USER, user);
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(PAGES + AUTH_JSP);
                requestDispatcher.forward(req, resp);
            }
        } catch (ServiceException e) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(PAGES + ERRORS + ERROR_LOGIN);
            requestDispatcher.forward(req, resp);
        }
    }
}
