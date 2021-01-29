package ru.clevertec.check.utils.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.observer.entity.State;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Date;
import java.util.Properties;

import static ru.clevertec.check.utils.mail.JavaMailProperties.*;

public class JavaMailUtil {
    private static Logger logger = LogManager.getLogger();

    public static void sendMail() throws ProductException {
        System.out.println("Preparing to send");
        try {
            Properties properties = getProperties(PROPS_FILE);
            String myAccountEmail = MY_EMAIL_ACCOUNT;
            String password = PASSWORD;
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });
            if (Boolean.valueOf(ATTACH)) {
                Message message = prepareMessageWithContent(session, myAccountEmail, EMAIL_RECIPIENT);
                Transport.send(message);
                System.out.println("Send successfully with PDF");
            } else {
                Message message = prepareMessageWithoutContent(session, myAccountEmail, EMAIL_RECIPIENT);
                Transport.send(message);
                System.out.println("Send successfully without PDF");
            }
        } catch (MessagingException e) {
            throw new ProductException("Messaging Exception", e);
        }
    }

    public static void sendMailToListener(String recipient, State eventType, String messageText, String filePath) throws ProductException {
        try {
            Properties properties = getProperties(PROPS_FILE);
            String myAccountEmail = MY_EMAIL_ACCOUNT;
            String password = PASSWORD;
            Date date = new Date();
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(eventType.toString());
            Multipart emailContent = new MimeMultipart();
            MimeBodyPart textBody = new MimeBodyPart();
            textBody.setText(date + "\n" + messageText);
            MimeBodyPart attach = new MimeBodyPart();
            attach.attachFile(filePath);
            emailContent.addBodyPart(textBody);
            emailContent.addBodyPart(attach);
            message.setContent(emailContent);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ProductException("Messaging Exception", e);
        } catch (IOException e) {
            throw new ProductException("IOException", e);
        }
    }

    private static Message prepareMessageWithContent(Session session, String myAccountMail, String recipient) throws ProductException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(SUBJECT);
            Multipart emailContent = new MimeMultipart();
            MimeBodyPart textBody = new MimeBodyPart();
            textBody.setText(MESSAGE_WITH_ATTACH);
            MimeBodyPart pdfAttach = new MimeBodyPart();
            pdfAttach.attachFile(CHECKFILEPDF);
            emailContent.addBodyPart(textBody);
            emailContent.addBodyPart(pdfAttach);
            message.setContent(emailContent);
            return message;
        } catch (MessagingException e) {
            throw new ProductException("Messaging Exception", e);
        } catch (IOException e) {
            throw new ProductException("IOException", e);
        }
    }

    private static Message prepareMessageWithoutContent(Session session, String myAccountMail, String recipient) throws ProductException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(SUBJECT);
            message.setText(MESSAGE_WITHOUT_ATTACH);
            return message;
        } catch (AddressException e) {
            throw new ProductException("Address Exception", e);
        } catch (MessagingException e) {
            throw new ProductException("Messaging Exception", e);
        }
    }
}
