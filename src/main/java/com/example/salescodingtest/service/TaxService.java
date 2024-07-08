package com.example.salescodingtest.service;

import com.example.salescodingtest.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Tax computation service
 */
@Service
@Configuration
public class TaxService {

    /**
     * Product catalog
     */
    protected final ProductCatalog productCatalog;

    /**
     * Default tax rate for imported products
     */
    @Value("${product.imported.taxrate}")
    protected Double importTaxRate;

    /**
     * Default constructor
     * @param productCatalog Product catalog service
     */
    public TaxService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    /**
     * Compute the tax for a given product and its price
     * @param product given Product
     * @param price product price without taxes
     * @return amount of taxes for the given product
     */
    public Double getTaxForProduct(Product product, Double price) {
        Double taxRate = product.productCategory().taxRate();
        Double tax = 0d;

        if (taxRate == null) {
            taxRate = 0d;
        }

        if (product.imported()) {
            taxRate += importTaxRate;
        }

        if (taxRate != 0d) {
            tax = Math.ceil(price * taxRate * 20.0) / 20.0;
        }

        return tax;
    }

    /**
     * Compute the bill with taxes for a given order
     * @param order given Order
     * @return Bill
     * @throws ResponseStatusException
     */
    public Bill getBillForOrder(Order order) throws ResponseStatusException {
        Bill bill = new Bill();
        List<OrderLine> orderLines = order.getProductList();

        if (orderLines.isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is empty");
        }

        Double totalTax = 0d;
        Double totalPrice = 0d;

        for (OrderLine orderLine: orderLines) {
            String productName = orderLine.product();
            Double price = orderLine.price();
            Integer count = orderLine.count();

            Product product = productCatalog.getProduct(productName);

            Double tax = getTaxForProduct(product, price);
            totalTax += tax;
            totalPrice += tax + price;

            bill.addProduct(productName, count, Math.round((price + tax) * 100) / 100.0);
        }

        bill.setTotalPrice(Math.round(totalPrice * 100) / 100.0);
        bill.setTotalTaxes(Math.round(totalTax * 100) / 100.0);

        return bill;
    }
}
