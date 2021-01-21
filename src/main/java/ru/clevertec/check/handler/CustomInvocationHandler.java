package ru.clevertec.check.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.parser.JsonParser;
import ru.clevertec.check.parser.impl.JsonParserImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomInvocationHandler implements InvocationHandler {
    public static final String COMPLETED ="Completed ";
    public static final String SPACE =" ";
    public static final String METHOD_IN = " method in ";
    public static final String RESULT = ". Result: ";
    public static final String CLASS_ARGS = " class with args";

    private Object obj;
    private Method method;
    static Logger logger = LogManager.getLogger();
    JsonParser jsonParser = new JsonParserImpl();
    public CustomInvocationHandler(Object obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPACE);
        String methodName = method.getName() +"()";
        String className = this.obj.getClass().getSimpleName();
        String methodReturn = method.getReturnType().getSimpleName();
        Object resultmethodInvoke = method.invoke(obj,args);
        String inputArgs = null;
        for(int i=0; i< args.length; i++){
            if(args[i].getClass().equals(StringBuilder.class)){
                stringBuilder.append("StringBuilder");
                stringBuilder.append(SPACE);
            }
            else if(args[i].getClass().equals(String[].class)){
                stringBuilder.append(args[i].getClass().getSimpleName());
                inputArgs = jsonParser.parseJson(args[i]);
                stringBuilder.append(inputArgs);
                stringBuilder.append(SPACE);
            }
            else{
                stringBuilder.append(args[i].getClass().getSimpleName());
                inputArgs = jsonParser.parseJson(args[i]);
                stringBuilder.append(inputArgs);
                stringBuilder.append(SPACE);
            }
        }
        if(method.getReturnType() != void.class){
            logger.info(COMPLETED + methodName + METHOD_IN
                    + className + CLASS_ARGS + stringBuilder.toString() +
                    RESULT + methodReturn + SPACE + resultmethodInvoke);
        }
        else{
            logger.info("COMPLETED  "+ methodName + METHOD_IN
                    + className + CLASS_ARGS + stringBuilder.toString());
        }
        return resultmethodInvoke;
    }
}
