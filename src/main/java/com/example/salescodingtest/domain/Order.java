package com.example.salescodingtest.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an order
 */
public class Order {

    /**
     * List of products ordered
     */
    protected List<OrderLine> productList;

    /**
     * Default constructor
     */
    public Order() {
        productList = new ArrayList<>();
    }

    /**
     * Add an order line for a given product
     * @param product Product name
     * @param count Number of ordered products
     * @param price Total price for the product
     */
    public void addProduct(String product, Integer count, Double price) {
        productList.add(new OrderLine(product, count, price));
    }

    public List<OrderLine> getProductList() {
        return productList;
    }
}
