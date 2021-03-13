package ru.clevertec.check.controller.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class AttributeSessionListeners implements HttpSessionAttributeListener {
    private final Logger log = LogManager.getLogger();

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        log.info(event.getName() + " " + event.getValue());
    }


}
