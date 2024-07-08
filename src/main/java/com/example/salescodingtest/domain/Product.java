package com.example.salescodingtest.domain;

/**
 * Record representing a product
 * @param name product name
 * @param imported true if product is imported, false otherwise
 * @param productCategory Product category
 */
public record Product(String name, Boolean imported, ProductCategory productCategory) {}
