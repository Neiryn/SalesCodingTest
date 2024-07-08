package com.example.salescodingtest.service;

import com.example.salescodingtest.domain.Product;
import com.example.salescodingtest.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductCatalogTest {

    @InjectMocks
    ProductCatalog productCatalog;

    @BeforeEach
    void init() {
        productCatalog.init();
    }

    @Test
    void testGetProductCategory() {
        ProductCategory productCategory = productCatalog.getProductCategory("Book");

        assertNotEquals(productCategory, null);
        assertEquals(productCategory.name(), "Book");
        assertEquals(productCategory.taxRate(), 0d);
    }

    @Test
    void testGetInvalidProductCategory() {
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            productCatalog.getProductCategory("Car");
        });
        assertTrue(exception.getMessage().contains("Product category not found"));
    }
    @Test
    void testGetProduct() {
        Product product = productCatalog.getProduct("book");

        assertNotEquals(product, null);
        assertEquals(product.name(), "book");
        assertEquals(product.productCategory().name(), "Book");
        assertFalse(product.imported());
    }
    @Test
    void testGetImportedProduct() {
        Product product = productCatalog.getProduct("imported bottle of perfume");

        assertNotEquals(product, null);
        assertEquals(product.name(), "imported bottle of perfume");
        assertTrue(product.imported());
    }

    @Test
    void testGetInvalidProduct() {
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            productCatalog.getProduct("car");
        });
        assertTrue(exception.getMessage().contains("Product not found"));
    }

}
