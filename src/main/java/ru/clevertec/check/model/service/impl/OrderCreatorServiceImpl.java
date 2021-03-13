package ru.clevertec.check.model.service.impl;

import ru.clevertec.check.annotations.log.LogLevel;
import ru.clevertec.check.annotations.log.LogMe;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.service.OrderCreatorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static ru.clevertec.check.model.service.CheckConstants.*;

public class OrderCreatorServiceImpl implements OrderCreatorService {

    @LogMe(LogLevel.ERROR)
    @Override
    public Map<String, Integer> makeOrder(List<String> list) throws ServiceException {
        BiFunction<Integer, Integer, Integer> bFunc = (oldValue, newValue) -> oldValue + newValue;
        Map<String, Integer> map = new HashMap<>();
        String[] temp;
        String key;
        int value;
        try {
            for (int i = 0; i <= list.size() - 1; i++) {
                temp = list.get(i).strip().split(QUANTITY_DELIMITER);
                try {
                    key = temp[0];
                    if (list.size() == 0) {
                        throw new ServiceException(PLEASE);
                    } else {
                        value = Integer.parseInt(temp[1]);
                        map.merge(key, value, bFunc);
                    }
                } catch (NumberFormatException e) {
                    throw new ServiceException(INCORRECT_PARAMS);
                }
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(INCORRECT_PARAMS);
        }
        return map;
    }
}
