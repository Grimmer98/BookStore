package com.example.bookstore.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Inventory;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.InventoryRepository;

@DataJpaTest
public class InventoryRepositoryTest {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testsaveAndFindInventory() {

        Book book = new Book(1L, "Alice in Wonderland", "Lewis Carroll", 50.0);
        Book savedBook = bookRepository.save(book);

        Inventory inventory = new Inventory(savedBook, 5);
        Inventory savedInventory = inventoryRepository.save(inventory);

        Optional<Inventory> foundInventory = inventoryRepository.findById(savedInventory.getBookId());
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getStock()).isEqualTo(5);
        assertThat(foundInventory.get().getBook().getTitle()).isEqualTo("Alice in Wonderland");
    }

    @Test
    public void testUpdateStock() {

        Book book = new Book(1L, "To Kill a Mockingbird", "Harper Lee", 12.0);
        Book savedBook = bookRepository.save(book);

        Inventory inventory = new Inventory(savedBook, 10);
        Inventory savedInventory = inventoryRepository.save(inventory);

        savedInventory.setStock(20);
        Inventory updatedInventory = inventoryRepository.save(savedInventory);

        Optional<Inventory> foundInventory = inventoryRepository.findById(updatedInventory.getBookId());
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getStock()).isEqualTo(20);
    }

    @Test
    public void testRemoveBookFromInventory() {
        Book book = new Book(1L, "Brave New World", "Aldous Huxley", 14.0);
        Book savedBook = bookRepository.save(book);

        Inventory inventory = new Inventory(savedBook, 7);
        Inventory savedInventory = inventoryRepository.save(inventory);

        inventoryRepository.delete(savedInventory);

        Optional<Inventory> foundInventory = inventoryRepository.findById(savedInventory.getBookId());
        assertThat(foundInventory).isNotPresent();
    }

    @Test
    public void testGetStock() {
        Book book = new Book(1L, "Brave New World", "Aldous Huxley", 14.0);
        Book savedBook = bookRepository.save(book);

        Inventory inventory = new Inventory(savedBook, 12);
        Inventory savedInventory = inventoryRepository.save(inventory);

        int stock = inventoryRepository.getStock(savedInventory.getBookId());
        assertThat(stock).isEqualTo(12);
    }

    @Test
    public void addBookToInventory() {
        Book book = new Book(1L, "Brave New World", "Aldous Huxley", 14.0);
        Book savedBook = bookRepository.save(book);

        int initialStock = 0;
        int addedQuantity = 10;
        inventoryRepository.addBookToInventory(savedBook, addedQuantity);

        Optional<Inventory> foundInventory = inventoryRepository.findById(savedBook.getId());
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getBook()).isEqualTo(savedBook);
        assertThat(foundInventory.get().getStock()).isEqualTo(initialStock + addedQuantity);
    }

}
