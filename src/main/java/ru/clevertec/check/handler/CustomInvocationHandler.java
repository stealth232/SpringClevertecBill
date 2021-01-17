package ru.clevertec.check.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CustomInvocationHandler implements InvocationHandler {
    private Object obj;
    static Logger logger = LogManager.getLogger();
    public CustomInvocationHandler(Object obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Completed  "+ method.getName() + "() method in " + this.obj.getClass().getName());
        return method.invoke(obj,args);
    }
}
