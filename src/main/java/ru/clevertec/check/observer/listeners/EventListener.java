package ru.clevertec.check.observer.listeners;

import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.observer.entity.State;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EventListener {
    void update(State eventType, String message) throws IOException, MessagingException, ProductException;
}
