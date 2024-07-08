package com.example.salescodingtest.service;

import com.example.salescodingtest.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TaxServiceTest {

    @Mock
    ProductCatalog productCatalog;

    @InjectMocks
    TaxService taxService;

    @BeforeEach
    void init() {
        //mock config property
        ReflectionTestUtils.setField(taxService, "importTaxRate", 0.05);
    }

    @Test
    void testGetTaxForBookProduct() {
        Product product = new Product("book", false, new ProductCategory("Book", 0d));
        assertEquals(taxService.getTaxForProduct(product, 15d), 0d);
    }
    @Test
    void testGetTaxForOtherProduct() {
        Product product = new Product("other", false, new ProductCategory("Other", 0.1d));
        assertEquals(taxService.getTaxForProduct(product, 18.99d), 1.9);
    }
    @Test
    void testGetTaxForImportedProduct() {
        Product product = new Product("other", true, new ProductCategory("Other", 0.1d));
        assertEquals(taxService.getTaxForProduct(product, 15d), 2.3);
    }

    @Test
    void testGetBillForOrderNoTax() {
        Order order = new Order();
        order.addProduct("book", 1, 15d);
        Product product = new Product("book", false, new ProductCategory("Book", 0d));

        when(productCatalog.getProduct("book")).thenReturn(product);

        Bill bill = taxService.getBillForOrder(order);

        assertNotEquals(bill, null);
        assertEquals(bill.getProductList().size(), 1);
        assertEquals(bill.getProductList().get(0).product(), "book");
        assertEquals(bill.getProductList().get(0).price(), 15d);
        assertEquals(bill.getProductList().get(0).count(), 1);
        assertEquals(bill.getTotalTaxes(), 0d);
        assertEquals(bill.getTotalPrice(), 15d);
    }

    @Test void testGetBillForOrderEmpty() {
        Order order = new Order();
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            taxService.getBillForOrder(order);
        });
        assertTrue(exception.getMessage().contains("Order is empty"));
    }

    @Test void testGetBillForOrderBasicTax() {
        Order order = new Order();
        order.addProduct("music CD", 2, 15d);
        Product product = new Product("music CD", false, new ProductCategory("Other", 0.1d));

        when(productCatalog.getProduct("music CD")).thenReturn(product);

        Bill bill = taxService.getBillForOrder(order);

        assertNotEquals(bill, null);
        assertEquals(bill.getProductList().size(), 1);
        assertEquals(bill.getProductList().get(0).product(), "music CD");
        assertEquals(bill.getProductList().get(0).price(), 16.5d);
        assertEquals(bill.getProductList().get(0).count(), 2);
        assertEquals(bill.getTotalTaxes(), 1.5);
        assertEquals(bill.getTotalPrice(), 16.5);
    }

    @Test void testGetBillForOrderMixedTax() {
        Order order = new Order();
        order.addProduct("imported bottle of perfume", 1, 27.99);
        order.addProduct("bottle of perfume", 1, 18.99);
        order.addProduct("packet of headache pills", 1, 9.75);
        order.addProduct("imported box of chocolates", 1, 11.25);
        Product product1 = new Product("packet of headache pills", false, new ProductCategory("Med", 0d));
        Product product2 = new Product("imported box of chocolates", true, new ProductCategory("Food", 0d));
        Product product3 = new Product("bottle of perfume", false, new ProductCategory("Other", 0.1));
        Product product4 = new Product("imported bottle of perfume", true, new ProductCategory("Other", 0.1));

        when(productCatalog.getProduct("packet of headache pills")).thenReturn(product1);
        when(productCatalog.getProduct("imported box of chocolates")).thenReturn(product2);
        when(productCatalog.getProduct("bottle of perfume")).thenReturn(product3);
        when(productCatalog.getProduct("imported bottle of perfume")).thenReturn(product4);

        Bill bill = taxService.getBillForOrder(order);

        assertNotEquals(bill, null);
        assertEquals(bill.getProductList().size(), 4);
        assertEquals(bill.getProductList().get(0).price(), 32.19d);
        assertEquals(bill.getProductList().get(1).price(), 20.89d);
        assertEquals(bill.getProductList().get(2).price(), 9.75d);
        assertEquals(bill.getProductList().get(3).price(), 11.85d);
        assertEquals(bill.getTotalTaxes(), 6.7);
        assertEquals(bill.getTotalPrice(), 74.68);
    }

}
