package ru.clevertec.check.controller.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    private final Logger log = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServiceFactory.getInstance().getUserService().getBaseUserList();
            ServiceFactory.getInstance().getProductService().getProductList();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
