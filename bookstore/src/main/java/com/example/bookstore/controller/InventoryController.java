package com.example.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.service.InventoryService;

@RestController
@RequestMapping("/books")
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<String> purchaseBook(@PathVariable Long id, @RequestParam int quantity) {
        boolean success = inventoryService.purchaseBook(id, quantity);
        if (success) {
            return ResponseEntity.ok("Purchase successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Purchase failed: insufficient stock or book not found");
        }
    }
}
