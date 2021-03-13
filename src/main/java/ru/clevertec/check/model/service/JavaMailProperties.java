package ru.clevertec.check.model.service;

import java.io.*;
import java.util.Properties;

public class JavaMailProperties {
    public static final String PROPS_FILE = "c:/Users/Stealth/IdeaProjects/NewCheck/src/main/resources/config.properties";
    public static Properties properties = getProperties(PROPS_FILE);
    public static final String MY_EMAIL_ACCOUNT = properties.getProperty("myAccountEmail");
    public static final String EMAIL_RECIPIENT = properties.getProperty("toMail");
    public static final String PASSWORD = properties.getProperty("password");
    public static final String MESSAGE_WITH_ATTACH = properties.getProperty("messageWithAttach");
    public static final String MESSAGE_WITHOUT_ATTACH = properties.getProperty("messageWithoutAttach");
    public static final String SUBJECT = properties.getProperty("subject");
    public static final String ATTACH = properties.getProperty("withAttach");
    public static final String MAIL_STEALTH = "stealth2322@gmail.com";
    public static final String MAIL_WHITE_RUSSIAN = "whiterussian2018@gmail.com";

    public static Properties getProperties(String emailPropertiesFilePath) {
        try (InputStream inputStream = new FileInputStream(emailPropertiesFilePath)) {
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            properties = new Properties();
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
