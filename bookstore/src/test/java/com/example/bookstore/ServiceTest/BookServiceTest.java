package com.example.bookstore.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;

import com.example.bookstore.service.BookService;

public class BookServiceTest {
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    public void getAllBooksTest() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "Book 1", "Author 1", 10.0));
        when(bookRepository.findAll()).thenReturn(bookList);

        List<Book> result = bookService.getAllBooks();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Book 1");
        assertThat(result.get(0).getAuthor()).isEqualTo("Author 1");
    }

    @Test
    public void getBookByIdTest() {
        Book book = new Book(1L, "Book 1", "Author 1", 10.0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);
        assertThat(result).isNotNull();
        assertThat(result.get().getTitle()).isEqualTo("Book 1");
        assertThat(result.get().getAuthor()).isEqualTo("Author 1");
    }

    @Test
    public void saveBookTest() {
        Book book = new Book(1L, "Book 1", "Author 1", 10.0);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Book 1");
        assertThat(result.getAuthor()).isEqualTo("Author 1");
    }

    @Test
    public void deleteBookTest() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);

    }

}
