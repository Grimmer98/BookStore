package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    default int getStock(Long bookId) {
        return findById(bookId).map(Inventory::getStock).orElse(0);
    }

    default void addBookToInventory(Book book, int quantity) {
        Inventory inventory = new Inventory();
        inventory.setBook(book);
        inventory.setStock(quantity);
        save(inventory);
    }

    default void updateStock(Long bookId, int newQuantity) {
        findById(bookId).ifPresent(inventory -> {
            inventory.setStock(newQuantity);
            save(inventory);
        });
    }

    default void removeBookFromInventory(Long bookId) {
        deleteById(bookId);
    }
}
