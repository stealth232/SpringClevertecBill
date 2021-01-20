package ru.clevertec.check.parser.impl;

import ru.clevertec.check.parser.JsonParser;
import java.lang.reflect.Field;
import java.util.*;

public class JsonParserImpl implements JsonParser {

    public static final String BRACE_OPEN = "{";
    public static final String BRACE_CLOSE = "}";
    public static final String SQUARE_BRACE_OPEN = "[";
    public static final String SQUARE_BRACE_CLOSE = "]";
    public static final String QUOTE = "\"";
    public static final String COMMA = ",";
    public static final String COLON = ":";

    @Override
    public String parseJson(Object object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        if (object == null) {
            return null;
        }
        if (Collection.class.isAssignableFrom(object.getClass())) {
            appendCollection(sb, object);
            return sb.toString();
        }

        if (Map.class.isAssignableFrom(object.getClass())) {
            appendMap(sb, object);
            return sb.toString();
        }
        String className = object.getClass().getSimpleName();
        switch (className) {
            case "String" :
                sb.append(QUOTE);
                sb.append(object);
                sb.append(QUOTE);
                return sb.toString();

            case "Integer":
                sb.append(object.toString());
                return sb.toString();

            case "int":
                sb.append(object.toString());
                return sb.toString();

            case "String[]":
                appendStringArray(sb, object);
                return sb.toString();

            case "Integer[]":
                appendIntegerArray(sb, object);
                return sb.toString();

            case "int[]" :
                appendIntArray(sb, object);
                return sb.toString();

            case "Boolean" :
                sb.append(object.toString());
                return sb.toString();

            case "boolean" :
                sb.append(object.toString());
                return sb.toString();
        }

        Field[] fields = object.getClass().getDeclaredFields();
        sb.append(BRACE_OPEN);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String name = fields[i].getName();
            Object value = fields[i].get(object);
            if (value != null) {
                sb.append(QUOTE);
                sb.append(name);
                sb.append(QUOTE);
                sb.append(COLON);
                markObject(sb, value);
                if (i < fields.length - 1) {
                    sb.append(COMMA);
                }
            }
        }
        sb.append(BRACE_CLOSE);
        return sb.toString();
    }

    private void markObject(StringBuilder sb, Object obj) throws IllegalAccessException {
        if (Collection.class.isAssignableFrom(obj.getClass())) {
            appendCollection(sb, obj);
        } else if (Map.class.isAssignableFrom(obj.getClass())) {
            appendMap(sb, obj);
        } else if (obj.getClass().isArray()) {
            switch (obj.getClass().getSimpleName()) {
                case "String[]" :
                    appendStringArray(sb, obj);
                    break;

                case "Integer[]" :
                    appendIntegerArray(sb, obj);
                    break;

                case "int[]" :
                    appendIntArray(sb, obj);
                    break;
            }
        } else {
            switch (obj.getClass().getSimpleName()) {
                case "String":
                    sb.append(QUOTE).append(obj).append(QUOTE);
                    break;

                case "Integer" :
                    sb.append(obj);
                    break;

                case "int" :
                    sb.append(obj);
                    break;

                case "Boolean":
                    sb.append(obj);
                    break;

                case "boolean" :
                    sb.append(obj);
                    break;
            }
        }
    }

    private void appendMap(StringBuilder sb, Object obj) throws IllegalAccessException {
        sb.append(BRACE_OPEN);
        Map<?, ?> map = (Map<?, ?>) obj;
        Set<?> keys = map.keySet();
        Object[] keysArray = keys.toArray();
        Object[] valuesArray = map.values().toArray();
        for (int i = 0; i < keysArray.length; i++) {
            sb.append(QUOTE);
            sb.append(keysArray[i]);
            sb.append(QUOTE);
            sb.append(COLON);
            markObject(sb, valuesArray[i]);
            if (i < keysArray.length - 1) {
                sb.append(COMMA);
            }
        }
        sb.append(BRACE_CLOSE);
    }

    private void appendCollection(StringBuilder sb, Object obj) throws IllegalAccessException {
        Collection<?> collection = (Collection<?>) obj;
        Object[] objects = collection.toArray();
        sb.append(SQUARE_BRACE_OPEN);
        for (int i = 0; i < objects.length; i++) {
            sb.append(parseJson(objects[i]));
            if (i < objects.length - 1) {
                sb.append(COMMA);
            }
        }
        sb.append(SQUARE_BRACE_CLOSE);
    }

    private void appendStringArray(StringBuilder sb, Object obj) {
        sb.append(SQUARE_BRACE_OPEN);
        String[] array = (String[]) obj;
        for (int i = 0; i < array.length; i++) {
            sb.append(QUOTE);
            sb.append(array[i]);
            sb.append(QUOTE);
            if (i < array.length - 1) {
                sb.append(COMMA);
            }
        }
        sb.append(SQUARE_BRACE_CLOSE);
    }

    private void appendIntegerArray(StringBuilder sb, Object obj) {
        sb.append(SQUARE_BRACE_OPEN);
        Integer[] array = (Integer[]) obj;
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(COMMA);
            }
        }
        sb.append(SQUARE_BRACE_CLOSE);
    }

    private void appendIntArray(StringBuilder sb, Object obj) {
        sb.append(SQUARE_BRACE_OPEN);
        int[] array = (int[]) obj;
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(COMMA);
            }
        }
        sb.append(SQUARE_BRACE_CLOSE);
    }
}
