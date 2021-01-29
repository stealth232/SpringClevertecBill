package ru.clevertec.check.observer.listeners;

import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.observer.entity.State;
import ru.clevertec.check.utils.mail.JavaMailUtil;

import static ru.clevertec.check.utils.mail.JavaMailProperties.CHECKFILETXT;

public class Consoler implements EventListener {

    @Override
    public void update(State eventType, String message) throws ProductException {
        JavaMailUtil.sendMailToListener("stealth2322@gmail.com", eventType,
                message, CHECKFILETXT);
    }
}
