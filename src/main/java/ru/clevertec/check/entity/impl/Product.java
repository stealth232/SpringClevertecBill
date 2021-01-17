package ru.clevertec.check.entity.impl;

import ru.clevertec.check.parameters.ProductParameters;

public abstract class Product implements ProductParameters {
    private int itemId;
    private String name;
    private double cost;
}
