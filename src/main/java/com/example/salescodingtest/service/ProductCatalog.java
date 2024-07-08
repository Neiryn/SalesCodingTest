package com.example.salescodingtest.service;

import com.example.salescodingtest.domain.Product;
import com.example.salescodingtest.domain.ProductCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Catalog of available products
 */
@Service
public class ProductCatalog {

    /**
     * Product catalog
     */
    Map<String, Product> catalog;

    /**
     * Product categories
     */
    Map<String, ProductCategory> productCategories;

    /**
     * Default constructor
     */
    public ProductCatalog() {
        catalog = new TreeMap<>();
        productCategories = new TreeMap<>();
    }

    /**
     * Register a product category
     * @param name category name
     * @param taxRate default tax rate
     */
    public void registerProductCategory(String name, Double taxRate) {
        ProductCategory productCategory = new ProductCategory(name, taxRate);
        productCategories.put(name, productCategory);
    }

    /**
     * Register a product
     * @param name product name
     * @param category product category
     * @param imported true if imported, false otherwise
     */
    public void registerProduct(String name, String category, Boolean imported) {
        ProductCategory productCategory = productCategories.get(category);
        Product product = new Product(name, imported, productCategory);
        catalog.put(name, product);
    }

    /**
     * Get a product category by its name
     * @param category category name
     * @return ProductCategory
     */
    public ProductCategory getProductCategory(String category) {
        if (!productCategories.containsKey(category)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product category not found");
        }
        return productCategories.get(category);
    }

    /**
     * Get a product by its name
     * @param product product name
     * @return Product
     */
    public Product getProduct(String product) throws ResponseStatusException {
        if (!catalog.containsKey(product)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return catalog.get(product);
    }

    /**
     * Catalog initialisation with sample categories and products
     */
    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<ProductCategory>> typeReference = new TypeReference<List<ProductCategory>>(){};
            TypeReference<List<List<String>>> typeReference2 = new TypeReference<List<List<String>>>(){};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/product_categories.json");
            InputStream inputStream2 = TypeReference.class.getResourceAsStream("/products.json");
            List<ProductCategory> categories = mapper.readValue(inputStream,typeReference);
            for (ProductCategory productCategory : categories) {
                productCategories.put(productCategory.name(), productCategory);
            }
            List<List<String>> products = mapper.readValue(inputStream2,typeReference2);
            for (List<String> product : products) {
                if (product.size() == 2) {
                    registerProduct(product.get(0), product.get(1), product.get(0).contains("imported"));
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
