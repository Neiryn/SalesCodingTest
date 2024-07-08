package com.example.salescodingtest.controller;

import com.example.salescodingtest.domain.Bill;
import com.example.salescodingtest.domain.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetBillForOrder() throws Exception {
        Order order = new Order();
        order.addProduct("book", 1, 10d);
        order.addProduct("imported bottle of perfume", 1, 15d);
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get("/tax/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTaxes").value("2.3"))
                .andExpect(jsonPath("$.totalPrice").value("27.3"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Bill bill = mapper.readValue(content, new TypeReference<Bill>() {});
        assertEquals(bill.getProductList().size(), 2);
        assertEquals(bill.getProductList().get(0).product(), "book");
        assertEquals(bill.getProductList().get(1).product(), "imported bottle of perfume");
    }

    @Test
    void testGetBillForOrderException() throws Exception {
        Order order = new Order();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(get("/tax/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("Order is empty")));
    }
}
