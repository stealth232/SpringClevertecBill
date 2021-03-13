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
import java.util.List;

import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebServlet(urlPatterns = ADMIN_USER)
public class AdminUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN);
        String loginToChange = req.getParameter(LOGIN_CHANGE);
        String userType = req.getParameter(ROLE);
        List<User> users = null;
        String message = null;
        RequestDispatcher request = req.getRequestDispatcher(PAGES + ADMIN + ADMIN_MESSAGE);
        if (login != null) {
            try {
                if (ServiceFactory.getInstance().getUserService().deleteByLogin(login)) {
                    message = "Delete " + login + " was succesfully";
                }
            } catch (ServiceException e) {
                message = "No user  " + login + " in our DB";
            }
        }
        if (loginToChange != null) {
            try {
                if (ServiceFactory.getInstance().getUserService().updateUserRole(loginToChange, userType)) {
                    message = "User " + loginToChange + " updated to " + userType;
                }
            } catch (ServiceException e) {
                message = "No user " + loginToChange + " or no role " + userType;
            }
        }
        try {
            users = ServiceFactory.getInstance().getUserService().getCurrentUserList();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        req.setAttribute(MESSAGE, message);
        req.setAttribute(USERS, users);
        request.forward(req, resp);

    }
}
