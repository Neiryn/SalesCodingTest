package com.example.salescodingtest.controller;

import com.example.salescodingtest.domain.Bill;
import com.example.salescodingtest.domain.Order;
import com.example.salescodingtest.service.TaxService;
import org.springframework.web.bind.annotation.*;

/**
 * Tax rest controller
 */
@RestController
@RequestMapping("/tax")
public class TaxController {

    /**
     * Tax computation service
     */
    final
    TaxService taxService;

    /**
     * Default controller
     * @param taxService Tax service
     */
    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    /**
     * Compute the full bill with taxes for a given order
     * Input and output as json with default mapping for Order and Bill
     * @param order Order
     * @return Bill details after computing taxes
     */
    @GetMapping("/order")
    public Bill getBillForOrder(@RequestBody Order order) {

        return taxService.getBillForOrder(order);
    }
}
