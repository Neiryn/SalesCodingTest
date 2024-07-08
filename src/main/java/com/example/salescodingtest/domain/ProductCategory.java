package com.example.salescodingtest.domain;

/**
 * Record representing a product category
 * @param name category name
 * @param taxRate dafault tax rate for the product category
 */
public record ProductCategory(String name, Double taxRate) {
}
