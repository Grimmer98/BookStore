package com.example.bookstore.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.InventoryRepository;
import com.example.bookstore.service.BookNotFoundException;

import com.example.bookstore.service.InventoryService;

public class InventoryServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBookToInventory_BookExists() {

        Long bookId = 1L;
        int quantity = 10;
        Book book = new Book(bookId, "1984", "George Orwell", 100.0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        inventoryService.addBookToInventory(bookId, quantity);

        verify(inventoryRepository, times(1)).addBookToInventory(book, quantity);
    }

    @Test
    public void testAddBookToInventory_BookNotFound() {

        Long bookId = 1L;
        int quantity = 10;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> inventoryService.addBookToInventory(bookId, quantity));
        verify(inventoryRepository, never()).addBookToInventory(any(Book.class), anyInt());
    }

    @Test
    public void testPurchaseBook_SufficientStock() {

        Long bookId = 1L;
        int quantity = 5;
        Book book = new Book(bookId, "1984", "George Orwell", 100.0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(inventoryRepository.getStock(bookId)).thenReturn(10);

        boolean result = inventoryService.purchaseBook(bookId, quantity);

        assertThat(result).isTrue();
        verify(inventoryRepository, times(1)).updateStock(bookId, 5);
        verify(inventoryRepository, never()).removeBookFromInventory(bookId);
    }

    @Test
    public void testPurchaseBook_TriggersRemoveBookWhenStockIsZero() {
        Long bookId = 1L;
        int initialStock = 5;
        int purchaseQuantity = 5;

        Book mockBook = new Book(bookId, "Test Book", "Test Author", 20.0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(inventoryRepository.getStock(bookId))
                .thenReturn(initialStock)
                .thenReturn(0);

        boolean result = inventoryService.purchaseBook(bookId, purchaseQuantity);

        verify(inventoryRepository, times(1)).updateStock(bookId, 0);

        verify(inventoryRepository, times(1)).removeBookFromInventory(bookId);

        assertTrue(result);
    }

    @Test
    public void testPurchaseBook_InsufficientStock() {

        Long bookId = 1L;
        int quantity = 10;
        Book book = new Book(bookId, "1984", "George Orwell", 100.0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(inventoryRepository.getStock(bookId)).thenReturn(5);

        boolean result = inventoryService.purchaseBook(bookId, quantity);

        assertThat(result).isFalse();
        verify(inventoryRepository, never()).updateStock(anyLong(), anyInt());
        verify(inventoryRepository, never()).removeBookFromInventory(anyLong());
    }

    @Test
    public void testPurchaseBook_BookNotFound() {

        Long bookId = 1L;
        int quantity = 10;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        boolean result = inventoryService.purchaseBook(bookId, quantity);

        assertThat(result).isFalse();
        verify(inventoryRepository, never()).updateStock(anyLong(), anyInt());
        verify(inventoryRepository, never()).removeBookFromInventory(anyLong());
    }

}
