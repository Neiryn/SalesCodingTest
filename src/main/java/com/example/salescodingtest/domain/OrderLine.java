package com.example.salescodingtest.domain;

/**
 * Record representing an order line for a given product
 * @param product product name
 * @param count number of ordered items
 * @param price price of the items
 */
public record OrderLine(String product, Integer count, Double price) {
}
