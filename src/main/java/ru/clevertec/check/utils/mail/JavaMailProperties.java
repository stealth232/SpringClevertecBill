package ru.clevertec.check.utils.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class JavaMailProperties {

    private static Logger logger = LogManager.getLogger();
    static final String PROPS_FILE = "c:/Users/Stealth/IdeaProjects/NewCheck/src/main/resources/config.properties";
    static Properties properties = getProperties(PROPS_FILE);
    static final String MY_EMAIL_ACCOUNT = properties.getProperty("myAccountEmail");
    static final String EMAIL_RECIPIENT = properties.getProperty("toMail");
    static final String PASSWORD = properties.getProperty("password");
    static final String MESSAGE_WITH_ATTACH = properties.getProperty("messageWithAttach");
    static final String MESSAGE_WITHOUT_ATTACH = properties.getProperty("messageWithoutAttach");
    static final String SUBJECT = properties.getProperty("subject");
    static final String ATTACH = properties.getProperty("withAttach");
    public static final String CHECKFILEPDF = "D:\\check.pdf";
    public static final String CHECKFILETXT = "D:\\check.txt";

    public static Properties getProperties(String emailProperties) {
        try (InputStream is = new FileInputStream(emailProperties)) {
            Reader reader = new InputStreamReader(is, "UTF-8");
            properties = new Properties();
            properties.load(reader);
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
