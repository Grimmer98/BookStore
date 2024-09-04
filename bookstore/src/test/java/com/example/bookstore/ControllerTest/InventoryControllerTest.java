package com.example.bookstore.ControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.bookstore.controller.InventoryController;
import com.example.bookstore.service.InventoryService;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {
    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private MockMvc mockMvc;

    @Test
    public void testPurchaseBook_Success() throws Exception {

        Long bookId = 1L;
        int quantity = 5;
        when(inventoryService.purchaseBook(bookId, quantity)).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();

        mockMvc.perform(post("/books/" + bookId + "/purchase")
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Purchase successful"));
    }

    @Test
    public void testPurchaseBook_Failure() throws Exception {

        Long bookId = 1L;
        int quantity = 5;
        when(inventoryService.purchaseBook(bookId, quantity)).thenReturn(false);

        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();

        mockMvc.perform(post("/books/" + bookId + "/purchase")
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Purchase failed: insufficient stock or book not found"));
    }
}
