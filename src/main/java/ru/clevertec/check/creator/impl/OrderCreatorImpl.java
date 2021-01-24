package ru.clevertec.check.creator.impl;

import ru.clevertec.check.annotations.LogLevel;
import ru.clevertec.check.annotations.LogMe;
import ru.clevertec.check.creator.OrderCreator;
import ru.clevertec.check.exception.ProductException;
import ru.clevertec.check.myLinkedList.impl.MyLinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class OrderCreatorImpl implements OrderCreator {
    static Logger logger = LogManager.getLogger();

    private static final String QUANTITY_DELIMITER = "-";
    @LogMe(LogLevel.ERROR)
    @Override
    public Map<String, Integer> order(MyLinkedList<String> list) throws ProductException {
        BiFunction<Integer, Integer, Integer> bFunc = (oldValue, newValue) -> oldValue + newValue;
        Map<String, Integer> map = new HashMap<>();
        String[] tmp;
        String key;
        int value;
        int d = 5;
        try {
            for (int i=0; i<= list.size()-1;i++) {
                tmp = list.get(i).strip().split(QUANTITY_DELIMITER);
                try {
                    key = tmp[0];
                    if (list.size()==0) {
                        throw new ProductException("Please try to buy something at our store");
                    }
                    if (!tmp[0].equals("card") && d < Integer.parseInt(tmp[0])) {
                        throw new ProductException("There are only 5 products in our test store. " +
                                "Please try again");
                    } else {
                        value = Integer.parseInt(tmp[1]);
                        map.merge(key, value, bFunc);
                    }
                } catch (NumberFormatException e) {
                    throw new ProductException("Parameters are incorrect. Please try again");
                }
            }
        } catch (NumberFormatException e) {
            throw new ProductException("Parameters are incorrect. Please try again");
        }
        return map;
    }

    public Map<String, Integer> createOrder(List<String> str) {
        Map<String, Integer> map = new HashMap<>();
        String[] tmp ;
        String key;
        int value;
        for (String s : str) {
            tmp = s.strip().split(QUANTITY_DELIMITER);

            key = tmp[0];
            value = Integer.parseInt(tmp[1]);
            map.put(key, value);
        }


        return map;
    }
}
