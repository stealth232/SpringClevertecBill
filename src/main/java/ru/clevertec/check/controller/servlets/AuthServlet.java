package ru.clevertec.check.controller.servlets;

import ru.clevertec.check.entities.user.User;
import ru.clevertec.check.entities.user.UserType;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = AUTH)
public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String login = req.getParameter(LOGIN);
            String password = req.getParameter(PASSWORD);
            User user = ServiceFactory.getInstance().getUserService().getUserByLogin(login, password);
            RequestDispatcher requestDispatcher;
            if (user != null && user.getUserType().equals(UserType.ADMIN)) {
                requestDispatcher = req.getRequestDispatcher(PAGES + ADMIN + CONTROLLER_JSP);
                req.setAttribute(USER, user);
            } else if (user != null && user.getUserType().equals(UserType.USER)) {
                requestDispatcher = req.getRequestDispatcher(PAGES + SHOP_JSP);
                req.setAttribute(USER, user);
            } else {
                requestDispatcher = req.getRequestDispatcher(PAGES + ERRORS + ERROR_AUTH_JSP);
            }
            requestDispatcher.forward(req, resp);
            System.out.println(user.getUserType());
            if (Objects.nonNull(req.getSession()) && Objects.isNull(req.getSession().getAttribute(USERTYPE))) {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute(USERTYPE, user.getUserType());
            }
        } catch (ServiceException e) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(PAGES + ERRORS + ERROR_LOGIN);
            requestDispatcher.forward(req, resp);
        }
    }
}
