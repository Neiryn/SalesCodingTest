package com.example.salescodingtest.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing bill details for an order
 */
public class Bill {

    /**
     * Products ordered with VAT prices
     */
    protected List<OrderLine> productList;

    /**
     * Total taxes for the order
     */
    protected Double totalTaxes;

    /**
     * Final VAT price for the order
     */
    protected Double totalPrice;

    /**
     * Default constructor
     */
    public Bill() {
        productList = new ArrayList<>();
    }

    /**
     * Add an order line for a given product
     * @param product Product name
     * @param count Number of products ordered
     * @param price Total VAT price for the product
     */
    public void addProduct(String product, Integer count, Double price) {
        productList.add(new OrderLine(product, count, price));
    }

    public List<OrderLine> getProductList() {
        return productList;
    }

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
