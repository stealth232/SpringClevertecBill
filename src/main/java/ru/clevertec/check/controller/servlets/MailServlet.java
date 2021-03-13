package ru.clevertec.check.controller.servlets;

import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.clevertec.check.controller.constants.ServletConstants.*;
import static ru.clevertec.check.model.service.CheckConstants.TEXT_HTML;

@WebServlet(urlPatterns = CHECK)
public class MailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(TEXT_HTML);
        RequestDispatcher request = req.getRequestDispatcher(PAGES + MAIL_MESS_JSP);
        String message;
        String mail = req.getParameter(MAIL);
        try {
            if (ServiceFactory.getInstance().getMailService().sendMailToUser(mail)) {
                message = "Successfully send on " + mail;
            } else {
                message = "Check was not send on " + mail;
            }
            req.setAttribute(MESSAGE, message);
            request.forward(req, resp);
        } catch (ServiceException e) {
            message = "Check was not send on " + mail;
            req.setAttribute(MESSAGE, message);
            request.forward(req, resp);
        }

    }
}
