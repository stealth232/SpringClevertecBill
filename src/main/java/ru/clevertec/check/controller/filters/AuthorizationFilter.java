package ru.clevertec.check.controller.filters;

import ru.clevertec.check.entities.user.UserType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static ru.clevertec.check.controller.constants.ServletConstants.*;

@WebFilter(urlPatterns = PAGES + ADMIN + "/*")
public class AuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if (Objects.nonNull(session)) {
            UserType userType = (UserType) session.getAttribute(USERTYPE);
            if (userType == UserType.ADMIN) {
                chain.doFilter(request, response);
            } else {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(PAGES + ERRORS + ERROR_ADMIN);
                requestDispatcher.forward(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
