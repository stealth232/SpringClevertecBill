package ru.clevertec.check.observer.listeners;

import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.ServiceFactory;
import ru.clevertec.check.observer.entity.State;

import static ru.clevertec.check.model.service.CheckConstants.CHECKFILETXT;
import static ru.clevertec.check.model.service.JavaMailProperties.MAIL_STEALTH;

public class Consoler implements EventListener {

    @Override
    public void update(State eventType, String message) throws ServiceException {
        ServiceFactory.getInstance().getMailService().sendMailToListener(MAIL_STEALTH, eventType,
                message, CHECKFILETXT);
    }
}
