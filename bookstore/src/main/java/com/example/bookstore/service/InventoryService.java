package com.example.bookstore.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.InventoryRepository;

@Service
public class InventoryService {

    private BookRepository bookRepository;
    private InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(BookRepository bookRepository, InventoryRepository inventoryRepository) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void addBookToInventory(Long bookId, int quantity) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            inventoryRepository.addBookToInventory(book, quantity);
        } else {
            throw new BookNotFoundException("Book not found");
        }
    }

    @Transactional
    public boolean purchaseBook(Long bookId, int quantity) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            int currentStock = inventoryRepository.getStock(bookId);
            if (currentStock >= quantity) {
                inventoryRepository.updateStock(bookId, currentStock - quantity);
                if (inventoryRepository.getStock(bookId) == 0) {
                    inventoryRepository.removeBookFromInventory(bookId);
                }
                return true;
            }
        }
        return false;
    }
}
